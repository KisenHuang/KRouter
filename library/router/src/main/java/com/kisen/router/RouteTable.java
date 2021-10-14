package com.kisen.router;

import android.app.Activity;
import android.text.TextUtils;

import java.util.Map;

/**
 * 跳转Uri与页面映射关系
 * Created by huang on 2017/3/27.
 */
public class RouteTable {

    private Map<String, Class<? extends Activity>> table = new ExceptionMap();

    public Class<? extends Activity> getClassByPath(String path) {
        return TextUtils.isEmpty(path) ? null : table.get(path);
    }

    public Map<String, Class<? extends Activity>> getMapping() {
        return table;
    }
}
