package com.kisen.router;

import android.net.Uri;

/**
 * 回调函数
 * Created by huang on 2017/3/27.
 */

public interface RouteCallback {
    /**
     * Callback
     *
     * @param state   {@link RouteResultState}
     * @param uri     Uri
     * @param message notice msg
     */
    void callback(RouteResultState state, Uri uri, String message);
}
