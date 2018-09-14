package com.zhqydot.framework.easyrouter.core.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.zhqydot.framework.easyrouter.core.safr.ActivityResultInfo;
import com.zhqydot.framework.easyrouter.core.safr.RxActivityResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

public class RouterManager {

    private static Map<String, Class> routeMap = new HashMap<>();
    private static RouterInterceptor interceptor;

    public static void route(Context context, String path) {
        route(context, path, null);
    }

    public static void route(Context context, String path, Bundle bundle) {
        if (interceptor == null || interceptor.onRoute(path)) {
            Class aClass = routeMap.get(path);
            if (aClass != null) {
                Intent intent = new Intent();
                intent.setClass(context, aClass);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                if (bundle != null) {
                    intent.putExtras(bundle);
                }
                context.startActivity(intent);
            } else {
                throw new RouteException();
            }
        }

    }

    public static Observable<ActivityResultInfo> routeForResult(Activity activity, String path, int requestCode) {
        return routeForResult(activity, path, null, requestCode);
    }

    public static Observable<ActivityResultInfo> routeForResult(Activity activity, String path, Bundle bundle, int requestCode) {
        Class clazz = routeMap.get(path);
        if (clazz != null) {
            Intent intent = new Intent(activity, clazz);
            if (bundle != null) {
                intent.putExtras(bundle);
            }
            return new RxActivityResult(activity).startForResult(intent, requestCode);
        } else {
            throw new RouteException();
        }
    }

    public static void register(String path, Class activity) {
        try {
            Class clazz = activity.asSubclass(Activity.class);
            routeMap.put(path, activity);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        init(null);
    }

    public static void init(RouterInterceptor interceptor) {
        RouterManager.interceptor = interceptor;
        try {
            Class clazz = Class.forName("com.zhqydot.framework.easyrouter.core.RouterLoader");
            Object obj = clazz.newInstance();
            Method method = clazz.getDeclaredMethod("load");
            method.invoke(obj);
        } catch (ClassNotFoundException | IllegalAccessException |
                InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            Log.e("RouteManager", e.getMessage());
        }
    }

}
