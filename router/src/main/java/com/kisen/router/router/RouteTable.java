package com.kisen.router.router;

import android.app.Activity;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 跳转Uri与页面映射关系
 * Created by huang on 2017/3/27.
 */
public class RouteTable {

    private Map<String, Class<? extends Activity>> table = new HashMap<>();

    /**
     * 添加路由映射关系
     * 如果重复，自动覆盖
     *
     * @param cell 单个映射关系类
     */
    public void addMapping(RouteCell cell) {
        if (TextUtils.isEmpty(cell.getPath()) || cell.getClazz() == null)
            return;
        table.put(cell.getPath(), cell.getClazz());
    }

    public Class<? extends Activity> getClassByPath(String path) {
        return TextUtils.isEmpty(path) ? null : table.get(path);
    }

    public Map<String, Class<? extends Activity>> getMapping() {
        return table;
    }
}
