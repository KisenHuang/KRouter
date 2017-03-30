package com.kisen.router.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface Router {
    String[] value();

    Permission[] permissions() default Permission.DEFAULT;

    enum Permission {
        /**
         * 默认没有权限
         */
        DEFAULT,
        /**
         * 设置登录权限
         */
        LOGIN,
        /**
         * 设置设计师权限
         */
        DESIGNER
    }
}
