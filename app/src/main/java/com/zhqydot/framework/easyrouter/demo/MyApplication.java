package com.zhqydot.framework.easyrouter.demo;

import android.app.Application;

import com.zhqydot.framework.easyrouter.core.common.EasyRouter;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        EasyRouter.init(this);
    }
}
