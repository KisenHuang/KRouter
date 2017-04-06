package com.kisen.router.compiler;

import com.google.auto.service.AutoService;
import com.kisen.router.annotation.Module;
import com.kisen.router.annotation.Modules;
import com.kisen.router.annotation.Router;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;
import com.squareup.javapoet.WildcardTypeName;

import java.io.IOException;
import java.util.ArrayList;
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

    private static final String DOT = ".";
    private static final String ACTIVITY_FULL_NAME = "android.app.Activity";
    private static final String PACKAGE_NAME = "com.kisen.router.router";
    private Messager messager;
    private Filer filer;
    private Elements elementUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        elementUtils = processingEnv.getElementUtils();
        messager = processingEnv.getMessager();
        filer = processingEnv.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<>();
        ret.add(Router.class.getCanonicalName());
        ret.add(Module.class.getCanonicalName());
        return ret;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        debug("process start");
        if (annotations.isEmpty()) {
            return false;
        }
        //module
        boolean hasModule = false;
        Set<? extends Element> moduleElements = roundEnvironment.getElementsAnnotatedWith(Module.class);
        String moduleName = "RouterMapping";
        for (Element moduleElement : moduleElements) {
            Module annotation = moduleElement.getAnnotation(Module.class);
            moduleName = moduleName + "_" + annotation.value();
            debug("currentModule = " + annotation.value());
            hasModule = true;
        }
        //modules
        boolean hasModules = false;
        Set<? extends Element> modulesElements = roundEnvironment.getElementsAnnotatedWith(Modules.class);
        String[] moduleNames = null;
        for (Element modulesElement : modulesElements) {
            Modules annotation = modulesElement.getAnnotation(Modules.class);
            moduleNames = annotation.value();
            hasModules = true;
            debug("moduleNames.length = " + moduleNames.length);
        }
        // RouterInit
        if (hasModules) {
            generateModulesRouterInit(moduleNames);
        } else if (!hasModule) {
            generateDefaultRouterInit();
        }
        // RouterMapping
        debug("generateRouteTable start");
        return generateRouteTable(moduleName, roundEnvironment);
    }

    private void generateDefaultRouterInit() {
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

        initMethod.addStatement("RouterMapping.generateMapping(list,map)");
        TypeSpec routerInit = TypeSpec.classBuilder("RouteInfo")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(initMethod.build())
                .build();
        try {
            JavaFile.builder("com.kisen.router.router.annotation", routerInit)
                    .build()
                    .writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateModulesRouterInit(String[] moduleNames) {
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
            initMethod.addStatement("RouterMapping_" + module + ".generateMapping(list,map)");
            debug("generateModulesRouterInit--" + module);
        }
        TypeSpec routerInit = TypeSpec.classBuilder("RouteInfo")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(initMethod.build())
                .build();
        try {
            JavaFile.builder("com.kisen.router.router.annotation", routerInit)
                    .build()
                    .writeTo(filer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * RouteTable.
     */
    private boolean generateRouteTable(String moduleName, RoundEnvironment roundEnv) {
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
            Router.Permission[] permissions = route.permissions();
            for (Router.Permission permission : permissions) {
                if (permission == Router.Permission.DEFAULT)
                    continue;
                generateMapping.addStatement("list.add($S)", className.simpleName() + permission.ordinal());
            }
        }
        pathlist.clear();

        TypeSpec type = TypeSpec.classBuilder(moduleName)
                .addModifiers(Modifier.PUBLIC)
                .addMethod(generateMapping.build())
                .addJavadoc("Generated by Router. Do not edit it!\n")
                .build();
        try {
            JavaFile.builder(PACKAGE_NAME + DOT + "annotation", type).build().writeTo(filer);
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
        messager.printMessage(Diagnostic.Kind.NOTE, msg);
    }

}
