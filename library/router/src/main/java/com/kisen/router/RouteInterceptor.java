package com.kisen.router;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 阻拦器
 * Created by huang on 2017/3/28.
 */

public interface RouteInterceptor {
    /**
     * @param context Context
     * @param uri     Uri
     * @param extras  Bundle
     * @return True if you want to intercept this route, false otherwise.
     */
    boolean interceptor(Context context, @NonNull Uri uri, @Nullable Bundle extras);
}
