package com.kisen.router.router.annotation;

import java.util.List;
import java.util.Map;

/**
 * 主要作用就是可以调用init方法
 * 利用注解+反射，程序编译时在app Module中生成了一个 RouterInfo 类，也具有init方法
 * 供RouterApi的init方法调用
 * <p>在编译生成代码之前，编译器会检测，本类只是一个替代品而已，无实际意义</p>
 * Created by huang on 2017/3/30.
 */
public class RouteInfo {
    public static void init(List<String> permission, Map mapping) {
    }
}
