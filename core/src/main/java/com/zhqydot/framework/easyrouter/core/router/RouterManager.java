package com.zhqydot.framework.easyrouter.core.router;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zhqydot.framework.easyrouter.core.safr.ActivityResultInfo;
import com.zhqydot.framework.easyrouter.core.safr.RxActivityResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;

/**
 * @author zhqy
 * @date 2018/9/16
 */

public class RouterManager {

    private static Context mContext;

    private static Map<String, Class> routeMap = new HashMap<>();

    public static RouteBuilder routeTo(@NonNull String path) {
        return new RouteBuilder(path);
    }

    public static RouteBuilder routeTo(Context context, @NonNull String path) {
        return new RouteBuilder(context, path);
    }

    protected static void navigation(RouteBuilder routeBuilder) {
        Class clazz = routeMap.get(routeBuilder.getPath());
        Context context = routeBuilder.getContext() == null ? mContext : routeBuilder.getContext();
        if (clazz != null) {
            Intent intent = new Intent();
            intent.setClass(context, clazz);
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            if (routeBuilder.getExtras() != null) {
                intent.putExtras(routeBuilder.getExtras());
            }
            context.startActivity(intent);
        } else {
            throw new RouteException("Unable to find an activity for the path, have you register before?");
        }
    }

    protected static Observable<ActivityResultInfo> navigationForResult(RouteBuilder routeBuilder, int requestCode) {
        Class clazz = routeMap.get(routeBuilder.getPath());
        Context context = routeBuilder.getContext();
        if (clazz != null) {
            if (!(context instanceof Activity)) {
                throw new RouteException("Navigation for result need the activity context to support.");
            } else {
                Activity activity = (Activity) context;
                Intent intent = new Intent(activity, clazz);
                if (routeBuilder.getExtras() != null) {
                    intent.putExtras(routeBuilder.getExtras());
                }
                return new RxActivityResult(activity).startForResult(intent, requestCode);
            }
        } else {
            throw new RouteException("Unable to find an activity for the path, have you register before?");
        }
    }

    public static void register(@NonNull String path, @NonNull Class<? extends Activity> activity) {
        try {
            Class clazz = activity.asSubclass(Activity.class);
            routeMap.put(path, activity);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public static void init(@NonNull Context context) {
        mContext = context.getApplicationContext();
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
