package com.kisen.router;

/**
 * @Title :
 * @Description :
 * @Version :
 * Created by huang on 2017/4/6.
 */

public class DoubleKeyException extends RuntimeException {

    public static String format = "the uri '%s' has be reuse,the previous is %s.class,the next is %s.class.";

    public DoubleKeyException(String path, String className1, String className2) {
        this(String.format(format, path, className1, className2));
    }

    public DoubleKeyException(String detailMessage) {
        super(detailMessage);
    }

    public DoubleKeyException(Throwable throwable) {
        super(throwable);
    }

    public DoubleKeyException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }
}
