package com.kisen.router;

import android.app.Activity;

import java.util.HashMap;

/**
 * 自定义异常Map
 * Created by huang on 2017/4/6.
 */

public class ExceptionMap extends HashMap<String, Class<? extends Activity>> {

    @Override
    public Class<? extends Activity> put(String key, Class<? extends Activity> value) {
        if (containsKey(key))
            throw new DoubleKeyException(key, value.getName(), get(key).getName());
        return super.put(key, value);
    }
}
