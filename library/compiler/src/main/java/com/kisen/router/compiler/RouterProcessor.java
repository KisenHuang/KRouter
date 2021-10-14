package com.kisen.router.compiler;

import com.google.auto.service.AutoService;
import com.kisen.router.annotations.BuildModule;
import com.kisen.router.annotations.Router;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class RouterProcessor extends AbstractProcessor {

    private static final String ACTIVITY_FULL_NAME = "android.app.Activity";
    public static final String PACKAGE_NAME = "com.kisen.router.stub";
    private static final String OPTION_NAME = "MODULE_NAME";
    private Messager messager;
    private Filer filer;
    private Elements elementUtils;
    private String moduleName;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
        moduleName = processingEnv.getOptions().get(OPTION_NAME);

        debug("RouterProcessor init");
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<>();
        ret.add(Router.class.getCanonicalName());
        ret.add(BuildModule.class.getCanonicalName());
        return ret;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.RELEASE_8;
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        debug("process start");
        if (annotations.isEmpty()) {
            return false;
        }
        //module
        debug("currentModule = " + moduleName);

        //RouterInit
        generateModulesRouterInit(roundEnvironment);

        // RouterMapping
        debug("generateRouteTable start");
        return generateRouteTable(roundEnvironment);
    }

    private void generateModulesRouterInit(RoundEnvironment roundEnv) {
        Set<? extends Element> modulesElements = roundEnv.getElementsAnnotatedWith(BuildModule.class);
        String[] moduleNames = null;
        for (Element modulesElement : modulesElements) {
            BuildModule annotation = modulesElement.getAnnotation(BuildModule.class);
            moduleNames = annotation.value();
            debug("moduleNames.length = " + moduleNames.length);
        }
        if (moduleNames == null)
            return;

        TypeElement activityType = elementUtils.getTypeElement(ACTIVITY_FULL_NAME);
        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(activityType))));

        ParameterizedTypeName listTypeName = ParameterizedTypeName.get(ClassName.get(List.class),
                ClassName.get(String.class));

        ParameterSpec mapParameterSpec = ParameterSpec.builder(mapTypeName, "map").build();
        ParameterSpec listParameterSpec = ParameterSpec.builder(listTypeName, "list").build();

        Set<ParameterSpec> parameterSpecs = new HashSet<>();
        parameterSpecs.add(mapParameterSpec);
        parameterSpecs.add(listParameterSpec);

        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                .addParameters(parameterSpecs);

        for (String module : moduleNames) {
            initMethod.addStatement("KRouter_" + module + ".generateMapping(list,map)");
            debug("generateModulesRouterInit--" + module);
        }
        //添加主模块
        initMethod.addStatement("KRouter_" + moduleName + ".generateMapping(list,map)");
        TypeSpec routerInit = TypeSpec.classBuilder("RouteInfo")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(initMethod.build())
                .build();
        try {
            JavaFile.builder(PACKAGE_NAME, routerInit)
                    .build()
                    .writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在module中创建路由表
     */
    private boolean generateRouteTable(RoundEnvironment roundEnv) {
        debug("generateRouteTable--" + moduleName);
        Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(Router.class);
        // Map<String, Class<? extends Activity>> map
        TypeElement activityType = elementUtils.getTypeElement(ACTIVITY_FULL_NAME);
        ParameterizedTypeName mapTypeName = ParameterizedTypeName.get(ClassName.get(Map.class),
                ClassName.get(String.class), ParameterizedTypeName.get(ClassName.get(Class.class),
                        WildcardTypeName.subtypeOf(ClassName.get(activityType))));

        ParameterizedTypeName listTypeName = ParameterizedTypeName.get(ClassName.get(List.class),
                ClassName.get(String.class));

        ParameterSpec mapParameterSpec = ParameterSpec.builder(mapTypeName, "map").build();
        ParameterSpec listParameterSpec = ParameterSpec.builder(listTypeName, "list").build();

        Set<ParameterSpec> parameterSpecs = new HashSet<>();
        parameterSpecs.add(mapParameterSpec);
        parameterSpecs.add(listParameterSpec);

        MethodSpec.Builder generateMapping = MethodSpec.methodBuilder("generateMapping")
                .addParameters(parameterSpecs)
                .addModifiers(Modifier.PUBLIC, Modifier.STATIC);
        Map<String, String> pathlist = new HashMap<>();
        for (Element element : elements) {
            Router route = element.getAnnotation(Router.class);
            String[] paths = route.value();
            ClassName className = ClassName.get((TypeElement) element);
            for (String path : paths) {
                if (pathlist.containsKey(path)) {
                    error(element, "the uri '%s' has be reuse,the previous is %s.class", path, pathlist.get(path));
                }
                pathlist.put(path, className.simpleName());
                generateMapping.addStatement("map.put($S, $T.class)", path, className);
            }
            String[] intercepts = route.intercept();
            for (String intercept : intercepts) {
                if (intercept.equals(""))
                    continue;
                generateMapping.addStatement("list.add($S)", className.simpleName() + intercept);
            }
        }
        pathlist.clear();

        TypeSpec type = TypeSpec.classBuilder("KRouter_" + moduleName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(generateMapping.build())
                .addJavadoc("Generated by Router. Do not edit it!\n")
                .build();
        try {
            JavaFile.builder(PACKAGE_NAME, type).build().writeTo(filer);
        } catch (IOException e) {
            e.printStackTrace();
            debug("generateRouteTable fail!");
        }
        return true;
    }

    private void error(Element element, String message, Object... args) {
        messager.printMessage(Diagnostic.Kind.ERROR, String.format(message, args), element);
    }

    private void debug(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE, String.format("KRouter : %s", msg));
    }

}
