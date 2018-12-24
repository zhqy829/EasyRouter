package com.zhqydot.framework.easyrouter.core.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.zhqydot.framework.easyrouter.core.filter.FilterManager;
import com.zhqydot.framework.easyrouter.core.filter.RouteFilter;
import com.zhqydot.framework.easyrouter.core.arp.ActivityResultInfo;
import com.zhqydot.framework.easyrouter.core.arp.RxActivityResult;
import com.zhqydot.framework.easyrouter.core.route.RouteBuilder;
import com.zhqydot.framework.easyrouter.core.route.RouterManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

/**
 * @author zhqy
 * @date 2018/9/16
 */

public class EasyRouter {

    public static final int RESULT_FILTER = -8;

    private static Context sContext;

    private static RouterManager sRouterManager = new RouterManager();
    private static FilterManager sFilterManager = new FilterManager();

    public static void init(@NonNull Context context) {
        sContext = context.getApplicationContext();
        List<String> classes = ClassUtils.getClasses(context, "com.zhqydot.framework.easyrouter.core.RouteLoader$");
        for (String className : classes) {
            try {
                Class clazz = Class.forName(className);
                if (IRouteLoader.class.isAssignableFrom(clazz)) {
                    IRouteLoader loader = (IRouteLoader) clazz.newInstance();
                    loader.load();
                }
            } catch (ClassNotFoundException | IllegalAccessException |
                    InstantiationException e) {
                e.printStackTrace();
                Log.e("EasyRouter", e.getMessage());
            }
        }
    }

    public static RouteBuilder routeTo(@NonNull String path) {
        return new RouteBuilder(path);
    }

    public static RouteBuilder routeTo(Context context, @NonNull String path) {
        return new RouteBuilder(context, path);
    }

    public static void navigation(RouteBuilder routeBuilder) {
        Class clazz = sRouterManager.getClassFromPath(routeBuilder);
        String path = routeBuilder.getPath();
        Bundle extras = routeBuilder.getExtras();
        if (clazz != null) {
            if (sFilterManager.doFilter(path,extras)) {
                Context context = routeBuilder.getContext() == null ? sContext : routeBuilder.getContext();
                Intent intent = new Intent();
                intent.setClass(context, clazz);
                if (!(context instanceof Activity)) {
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                }
                if (extras != null) {
                    intent.putExtras(extras);
                }
                context.startActivity(intent);
            }
        } else {
            throw new RouterException("Unable to find an activity for the path, have you register before?");
        }
    }

    public static Observable<ActivityResultInfo> navigationForResult(RouteBuilder routeBuilder, int requestCode) {
        Class clazz = sRouterManager.getClassFromPath(routeBuilder);
        String path = routeBuilder.getPath();
        Bundle extras = routeBuilder.getExtras();
        if (clazz != null) {
            if (!sFilterManager.doFilter(path, extras)) {
                return Observable.just(new ActivityResultInfo(requestCode, RESULT_FILTER, null));
            } else {
                Context context = routeBuilder.getContext();
                if (!(context instanceof Activity)) {
                    throw new RouterException("Navigation for result need the activity context to support.");
                } else {
                    Activity activity = (Activity) context;
                    Intent intent = new Intent(activity, clazz);
                    if (extras != null) {
                        intent.putExtras(extras);
                    }
                    return new RxActivityResult(activity).startForResult(intent, requestCode);
                }
            }
        } else {
            throw new RouterException("Unable to find an activity for the path, have you register before?");
        }
    }

    public static void register(@NonNull String path, @NonNull Class<? extends Activity> activity) {
        sRouterManager.register(path, activity);
    }

    public static void addFilter(RouteFilter filter, int priority) {
        sFilterManager.addFilter(filter, priority);
    }

    public static void removeFilter(RouteFilter filter) {
        sFilterManager.removeFilter(filter);
    }

    public static void addFilter(RouteFilter filter, String group, int priority) {
        sFilterManager.addFilter(filter, group, priority);
    }

    public static void removeFilter(RouteFilter filter, String group) {
        sFilterManager.removeFilter(filter, group);
    }

}
