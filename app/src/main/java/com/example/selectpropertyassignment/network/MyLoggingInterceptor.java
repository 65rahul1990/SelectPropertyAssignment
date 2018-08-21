package com.example.selectpropertyassignment.network;

import com.example.selectpropertyassignment.BuildConfig;
import com.ihsanbal.logging.LoggingInterceptor;

import okhttp3.internal.platform.Platform;

public class MyLoggingInterceptor {
    public static LoggingInterceptor provideOkHttpLogging(){
        return new LoggingInterceptor.Builder()
                .loggable(BuildConfig.DEBUG)
                .log(Platform.INFO)
                .request("Request")
                .response("Response")
                .build();
    }
}
