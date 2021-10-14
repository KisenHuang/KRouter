package com.kisen.krouter;

import android.app.Application;

import com.kisen.krouter.interceptor.LoginInterceptor;
import com.kisen.router.RouterApi;
import com.kisen.router.annotations.BuildModule;


/**
 * 程序入口
 * Created by huang on 2017/3/28.
 */
@BuildModule({"casemodule", "designermodule", "loginmodule"})
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RouterApi.initialize();
        RouterApi.setDebug(true);
        RouterApi.addInterceptor(LoginInterceptor.getInstance());
    }
}
