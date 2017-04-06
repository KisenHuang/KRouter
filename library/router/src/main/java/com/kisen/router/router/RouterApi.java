package com.kisen.router.router;

import android.app.Activity;
import android.net.Uri;

import com.kisen.router.router.annotation.RouteInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Router API 类
 * Created by huang on 2017/3/27.
 */
public class RouterApi {

    private static final List<RouteInterceptor> mInterceptors = new ArrayList<>();

    /**
     * 初始化路由器
     * 从配置中读取路由表、权限列表
     * <p>
     * 必须调用该方法
     * </p>
     */
    public static synchronized void initialize() {
        List<String> permissions = RouteHub.getInstance().getPermissions();
        Map<String, Class<? extends Activity>> mapping = RouteHub.getInstance().getMapping();
        if (!mapping.isEmpty() || !permissions.isEmpty()) {
            return;
        }
        RouteInfo.init(permissions, mapping);
        RouteMonitor.getInstance().setupMapping(mapping);
    }

    /**
     * 创建路由对象
     *
     * @param uri 目标命令
     * @return 路由
     */
    public static Router build(Uri uri) {
        return Router.getInstance().build(uri);
    }

    @SuppressWarnings("unused")
    public static Router build(String path) {
        return build(path == null ? null : Uri.parse(path));
    }

    /**
     * 设置是否开启测试模式
     *
     * @param debug 是否开启
     */
    @SuppressWarnings("unused")
    public static void setDebug(boolean debug) {
        RouteLog.setDebug(debug);
    }

    public static void printMonitor() {
        RouteMonitor.getInstance().printAll();
    }

    /**
     * 添加拦截器
     *
     * @param interceptor 拦截器
     */
    @SuppressWarnings("unused")
    public static void addInterceptor(RouteInterceptor interceptor) {
        if (mInterceptors.contains(interceptor))
            return;
        mInterceptors.add(interceptor);
    }

    static List<RouteInterceptor> getInterceptors() {
        return mInterceptors;
    }
}
