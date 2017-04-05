package com.kisen.router.router;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 管理路由映射关系
 * Created by huang on 2017/3/27.
 */
public class RouteHub {

    private static RouteHub mInstance;
    private RouteTable routeTable = new RouteTable();
    private List<String> permissions = new ArrayList<>();

    public static RouteHub getInstance() {
        if (mInstance == null)
            synchronized (RouteHub.class) {
                if (mInstance == null)
                    mInstance = new RouteHub();
            }
        return mInstance;
    }

    private RouteHub() {
    }

    Map<String, Class<? extends Activity>> getMapping() {
        return routeTable.getMapping();
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public RouteTable getRouteTable() {
        return routeTable;
    }
}
