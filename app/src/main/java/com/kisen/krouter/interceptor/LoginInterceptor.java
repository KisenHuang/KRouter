package com.kisen.krouter.interceptor;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.kisen.router.RouteHub;
import com.kisen.router.RouteInterceptor;
import com.kisen.router.RouterApi;
import com.kisen.router.annotations.Router;

import java.util.List;

/**
 * 登录验证拦截器
 * Created by huang on 2017/3/28.
 */
public class LoginInterceptor implements RouteInterceptor {

    private static LoginInterceptor mInstance;

    public static LoginInterceptor getInstance() {
        if (mInstance == null) {
            synchronized (LoginInterceptor.class) {
                if (mInstance == null)
                    mInstance = new LoginInterceptor();
            }
        }
        return mInstance;
    }

    private LoginInterceptor() {
    }

    @Override
    public boolean interceptor(Context context, @NonNull Uri uri, @Nullable Bundle extras) {
        List<String> permissions = RouteHub.getInstance().getIntercepts();
        if (permissions == null || permissions.isEmpty())
            return false;
        Class<? extends Activity> clazz = RouteHub.getInstance().getRouteTable().getClassByPath(uri.toString());
        if (clazz == null)
            return false;
        boolean needLogin = permissions.contains(clazz.getSimpleName() + "login");
        if (needLogin) {
            if (extras == null)
                extras = new Bundle();
            extras.putString("target", uri.toString());
            RouterApi.build("login")
                    .extras(extras)
                    .setSkipInterceptors(true)
                    .start(context);
        }
        return needLogin;
    }

}
