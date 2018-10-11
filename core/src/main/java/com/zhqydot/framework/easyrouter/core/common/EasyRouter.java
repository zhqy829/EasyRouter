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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

/**
 * @author zhqy
 * @date 2018/9/16
 */

public class EasyRouter {

    public static final int RESULT_FILTER = -8;
    private static final String PATH_REGEX = "/[a-zA-Z0-9]+/[a-zA-Z0-9]+";

    private static Context mContext;

    private static FilterManager filterManager = new FilterManager();
    private static Map<String, Map<String, Class>> routeMap = new HashMap<>();

    public static RouteBuilder routeTo(@NonNull String path) {
        return new RouteBuilder(path);
    }

    public static RouteBuilder routeTo(Context context, @NonNull String path) {
        return new RouteBuilder(context, path);
    }

    public static void navigation(RouteBuilder routeBuilder) {
        Class clazz = null;
        String path = routeBuilder.getPath();
        Bundle extras = routeBuilder.getExtras();
        Pattern pattern = Pattern.compile(PATH_REGEX);
        if (!pattern.matcher(path).matches()) {
            throw new RouterException("Can not resolve the path [" + routeBuilder.getPath() + "].");
        }
        String[] array = path.replaceFirst("/", "").split("/");
        Map<String, Class> groupMap = routeMap.get(array[0]);
        if (groupMap != null) {
            clazz = groupMap.get(array[1]);
        }
        if (clazz != null) {
            if (filterManager.doFilter(path,extras)) {
                Context context = routeBuilder.getContext() == null ? mContext : routeBuilder.getContext();
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
        Class clazz = null;
        String path = routeBuilder.getPath();
        Bundle extras = routeBuilder.getExtras();
        Pattern pattern = Pattern.compile(PATH_REGEX);
        if (!pattern.matcher(path).matches()) {
            throw new RouterException("Can not resolve the path [" + routeBuilder.getPath() + "].");
        }
        String[] array = path.replaceFirst("/", "").split("/");
        Map<String, Class> groupMap = routeMap.get(array[0]);
        if (groupMap != null) {
            clazz = groupMap.get(array[1]);
        }
        if (clazz != null) {
            if (!filterManager.doFilter(path, extras)) {
                PublishSubject<ActivityResultInfo> subject = PublishSubject.create();
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
        Pattern pattern = Pattern.compile(PATH_REGEX);
        if (!pattern.matcher(path).matches()) {
            throw new RouterException("Can not resolve the path [" + path + "], have you modify the Java file created by EasyRouter or call the register method manually?");
        }
        String[] array = path.replaceFirst("/", "").split("/");
        Map<String, Class> groupMap = routeMap.get(array[0]);
        if (groupMap == null) {
            groupMap = new HashMap<>();
            groupMap.put(array[1], activity);
            routeMap.put(array[0], groupMap);
        } else {
            groupMap.put(array[1], activity);
        }
    }

    public static void init(@NonNull Context context) {
        mContext = context.getApplicationContext();
        List<String> classes = ClassUtils.getClasses(context, "com.zhqydot.framework.easyrouter.core.RouteLoader$$");
        for (String className : classes) {
            try {
                Class clazz = Class.forName(className);
                Object obj = clazz.newInstance();
                Method method = clazz.getDeclaredMethod("load");
                method.invoke(obj);
            } catch (ClassNotFoundException | IllegalAccessException |
                    InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
                Log.e("EasyRouter", e.getMessage());
            }
        }
    }

    public static void addFilter(RouteFilter filter, int priority) {
        filterManager.addFilter(filter, priority);
    }

    public static void removeFilter(RouteFilter filter) {
        filterManager.removeFilter(filter);
    }

    public static void addFilter(RouteFilter filter, String group, int priority) {
        filterManager.addFilter(filter, group, priority);
    }

    public static void removeFilter(RouteFilter filter, String group) {
        filterManager.removeFilter(filter, group);
    }

}
