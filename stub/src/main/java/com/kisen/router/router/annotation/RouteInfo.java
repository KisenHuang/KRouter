package com.kisen.router.router.annotation;

import java.util.List;
import java.util.Map;

/**
 * 主要作用就是可以调用init方法
 * 利用注解+反射，程序编译时在app Module中生成RouteInfo
 * 供RouterApi的init方法调用
 * <p>只在编译时有作用，不会被打包</p>
 * Created by huang on 2017/3/30.
 */
public class RouteInfo {
    public static void init(List<String> permission, Map mapping) {
    }
}
