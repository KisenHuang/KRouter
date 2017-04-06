package com.kisen.router;

import android.app.Application;

import com.kisen.router.annotation.Modules;
import com.kisen.router.interceptor.LoginInterceptor;
import com.kisen.router.router.RouterApi;

/**
 * 程序入口
 * Created by huang on 2017/3/28.
 */
@Modules({"app", "case", "designer", "login"})
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RouterApi.initialize();
        RouterApi.setDebug(true);
        RouterApi.addInterceptor(LoginInterceptor.getInstance());
    }
}
