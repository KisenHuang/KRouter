package com.kisen.router.router;

import android.app.Activity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 路由监控器
 * <p>
 * 用于监控路由状态，统计数据
 * </p>
 * Created by huang on 2017/4/5.
 */

public class RouteMonitor {

    private final static String TAG = RouteMonitor.class.getSimpleName();
    private static RouteMonitor instance;
    private Map<String, Class<? extends Activity>> mapping;
    private Map<String, Integer[]> table;
    private int totalCount;

    static RouteMonitor getInstance() {
        if (instance == null)
            synchronized (RouteMonitor.class) {
                if (instance == null)
                    instance = new RouteMonitor();
            }
        return instance;
    }

    private RouteMonitor() {
    }

    void setupMapping(Map<String, Class<? extends Activity>> map) {
        mapping = map;
        generateTable();
    }

    private void generateTable() {
        if (mapping != null && mapping.size() != 0) {
            table = new HashMap<>();
        }
    }

    public void write(String path) {
        //检测Uri合法性
        if (!mapping.containsKey(path)) {
            error("Uri不合法性");
            return;
        }
        totalCount++;
        if (table.containsKey(path)) {
            Integer[] count = table.get(path);
            count[0]++;
        } else {
            Integer[] count = new Integer[2];
            count[0] = 1;
            table.put(path, count);
        }
    }

    public void printAll() {
        String format = "%s\t%s\t%s\t%s%%\n";
        StringBuilder builder = new StringBuilder();
        builder.append("统计表数据\n")
                .append("uri\t\tactivity\tcount\tpercent\n");
        for (String key : mapping.keySet()) {
            String activityName = mapping.get(key).getSimpleName();
            if (table.containsKey(key)) {
                int count = table.get(key)[0];
                float percent = count * 100f / totalCount;
                builder.append(String.format(format, key, activityName, count, percent));
            } else {
                builder.append(String.format(format, key, activityName, 0, 0));
            }
        }
        RouteLog.i(TAG, builder.toString());
    }

    private void error(String msg) {
        RouteLog.i(TAG, msg);
    }

}
