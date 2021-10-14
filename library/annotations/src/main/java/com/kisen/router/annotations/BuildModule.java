package com.kisen.router.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description:
 * author: kisenhuang
 * email: Kisenhuang@163.com
 * time: 2021/10/13 7:39 下午
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface BuildModule {
    String[] value();
}
