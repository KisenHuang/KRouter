package com.kisen.router;

import android.os.Bundle;

import androidx.annotation.IntDef;
import androidx.annotation.Nullable;

/**
 * 页面跳转配置
 * Created by huang on 2017/3/27.
 */
public class RouteOptions {

    private int flags;
    @Nullable
    private RouteCallback callback;
    @Nullable
    private Bundle bundle;
    private boolean skipInterceptors;

    private int enterAnim;
    private int exitAnim;

    public void addFlags(int flags) {
        this.flags |= flags;
    }

    public int getFlags() {
        return flags;
    }

    public void setCallback(@Nullable RouteCallback callback) {
        this.callback = callback;
    }

    @Nullable
    public RouteCallback getCallback() {
        return callback;
    }

    public void setBundle(@Nullable Bundle bundle) {
        this.bundle = bundle;
    }

    @Nullable
    public Bundle getBundle() {
        return bundle;
    }

    public boolean isSkipInterceptors() {
        return skipInterceptors;
    }

    public void setSkipInterceptors(boolean skipInterceptors) {
        this.skipInterceptors = skipInterceptors;
    }

    public void setAnim(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
    }

    public int getEnterAnim() {
        return enterAnim;
    }

    public int getExitAnim() {
        return exitAnim;
    }
}
