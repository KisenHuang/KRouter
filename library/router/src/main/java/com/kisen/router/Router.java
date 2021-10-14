package com.kisen.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.AnimRes;

import java.util.List;

/**
 * Created by huang on 2017/3/27.
 */
public class Router {

    private static Router mInstance;
    private Uri uri;
    private RouteOptions mOptions;

    static Router getInstance() {
        if (mInstance == null)
            synchronized (Router.class) {
                if (mInstance == null) {
                    mInstance = new Router();
                }
            }
        mInstance.reset();
        return mInstance;
    }

    private Router() {
    }

    private void reset() {
        uri = null;
        mOptions = new RouteOptions();
    }

    Router build(Uri uri) {
        this.uri = uri;
        return this;
    }

    @SuppressWarnings("unused")
    public Router setCallback(RouteCallback callback) {
        mOptions.setCallback(callback);
        return this;
    }

    /**
     * Set skip the interceptors
     *
     * @param skip
     * @return
     */
    @SuppressWarnings("unused")
    public Router setSkipInterceptors(boolean skip) {
        mOptions.setSkipInterceptors(skip);
        return this;
    }

    /**
     * Add extra bundles.
     *
     * @param extras Bundle
     * @return this
     */
    @SuppressWarnings("unused")
    public Router extras(Bundle extras) {
        mOptions.setBundle(extras);
        return this;
    }

    /**
     * Add additional flags to the intent (or with existing flags value).
     *
     * @param flags The new flags to set, such as {@link Intent#FLAG_ACTIVITY_CLEAR_TOP}
     * @return this
     * @see Intent#addFlags(int)
     */
    @SuppressWarnings("unused")
    public Router addFlags(int flags) {
        mOptions.addFlags(flags);
        return this;
    }

    /**
     * Specify an explicit transition animation.
     *
     * @param enterAnim A resource ID of the animation resource to use for the incoming activity.
     *                  Use 0 for no animation.
     * @param exitAnim  A resource ID of the animation resource to use for the outgoing activity.
     *                  Use 0 for no animation.
     * @return this
     * @see Activity#overridePendingTransition(int, int)
     */
    public Router anim(@AnimRes int enterAnim, @AnimRes int exitAnim) {
        mOptions.setAnim(enterAnim, exitAnim);
        return this;
    }

    public void startForResult(Context context, int requestCode) {
        if (context == null)
            throw new NullPointerException("context can not be null!");
        Intent intent = getIntent(context);
        if (intent == null)
            return;
        RouteMonitor.getInstance().write(uri.toString());
        if (requestCode >= 0 && context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, requestCode);
        } else {
            context.startActivity(intent);
        }
        if (mOptions.getEnterAnim() != 0 && mOptions.getExitAnim() != 0
                && context instanceof Activity) {
            // Add transition animation.
            ((Activity) context).overridePendingTransition(
                    mOptions.getEnterAnim(), mOptions.getExitAnim());
        }
        callback(RouteResultState.SUCCEED, uri, null);
    }

    public void start(Context context) {
        startForResult(context, -1);
    }

    private Intent getIntent(Context context) {
        Intent intent = new Intent();
        if (uri == null) {
            callback(RouteResultState.FAILED, null, "Uri is null.");
            return null;
        }
        Class<? extends Activity> clazz = RouteHub.getInstance()
                .getRouteTable().getClassByPath(uri.toString());
        if (clazz == null) {
            callback(RouteResultState.FAILED, uri, "The mapping error,the activity can not find.");
            return null;
        }
        if (!mOptions.isSkipInterceptors()) {
            List<RouteInterceptor> interceptors = RouterApi.getInterceptors();
            for (RouteInterceptor interceptor : interceptors) {
                if (interceptor.interceptor(context, uri, mOptions.getBundle())) {
                    callback(RouteResultState.INTERCEPTED, uri, "Intercepted by global interceptor.");
                    return null;
                }
            }
        }
        intent.setClass(context, clazz);
        intent.setFlags(mOptions.getFlags());
        Bundle bundle = mOptions.getBundle();
        if (bundle != null)
            intent.putExtras(bundle);
        return intent;
    }

    private void callback(RouteResultState state, Uri uri, String msg) {
        if (state != RouteResultState.SUCCEED) {
            RouteLog.w(msg);
        }
        RouteCallback callback = mOptions.getCallback();
        if (callback == null)
            return;
        callback.callback(state, uri, msg);
    }
}
