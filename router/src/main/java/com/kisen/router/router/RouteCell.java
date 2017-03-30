package com.kisen.router.router;

import android.app.Activity;

/**
 * 一组路由跳转的映射关系
 * Created by huang on 2017/3/27.
 */

public class RouteCell {

    private String path;
    private Class<? extends Activity> clazz;

    public RouteCell(String path, Class<? extends Activity> clazz) {
        this.path = path;
        this.clazz = clazz;
    }

    public String getPath() {
        return path;
    }

    public Class<? extends Activity> getClazz() {
        return clazz;
    }
}
