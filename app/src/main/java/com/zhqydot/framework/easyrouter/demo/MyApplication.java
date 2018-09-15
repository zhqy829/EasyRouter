package com.zhqydot.framework.easyrouter.demo;

import android.app.Application;

import com.zhqydot.framework.easyrouter.core.router.RouterManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RouterManager.init(this);
    }
}
