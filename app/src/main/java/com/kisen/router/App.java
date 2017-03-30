package com.kisen.router;

import android.app.Application;

import com.kisen.router.annotation.Modules;
import com.kisen.router.router.RouterApi;
import com.kisen.router.interceptor.LoginInterceptor;

/**
 * 程序入口
 * Created by huang on 2017/3/28.
 */
@Modules({"app", "case", "designer", "login"})
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        RouteCell cell1 = new RouteCell("main", MainActivity.class);
//        RouteCell cell2 = new RouteCell("case", CaseActivity.class);
//        RouteCell cell3 = new RouteCell("designer", DesignerActivity.class);
//        RouterApi.addRouteTable(cell1);
//        RouterApi.addRouteTable(cell2);
//        RouterApi.addRouteTable(cell3);
        RouterApi.initialize();
        RouterApi.addInterceptor(LoginInterceptor.getInstance());
    }
}
