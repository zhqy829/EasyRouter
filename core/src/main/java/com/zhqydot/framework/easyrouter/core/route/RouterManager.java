package com.zhqydot.framework.easyrouter.core.route;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.zhqydot.framework.easyrouter.core.common.RouterException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class RouterManager {

    private static final String PATH_REGEX = "/[a-zA-Z0-9]+/[a-zA-Z0-9]+";

    private Map<String, Map<String, Class>> mRouteMap = new HashMap<>();

    public Class getClassFromPath(RouteBuilder routeBuilder) {
        Class clazz = null;
        String path = routeBuilder.getPath();
        Pattern pattern = Pattern.compile(PATH_REGEX);
        if (!pattern.matcher(path).matches()) {
            throw new RouterException("Can not resolve the path [" + routeBuilder.getPath() + "].");
        }
        String[] array = path.replaceFirst("/", "").split("/");
        Map<String, Class> groupMap = mRouteMap.get(array[0]);
        if (groupMap != null) {
            clazz = groupMap.get(array[1]);
        }
        return clazz;
    }

    public void register(@NonNull String path, @NonNull Class<? extends Activity> activity) {
        Pattern pattern = Pattern.compile(PATH_REGEX);
        if (!pattern.matcher(path).matches()) {
            throw new RouterException("Can not resolve the path [" + path + "], have you modify the Java file created by EasyRouter or call the register method manually?");
        }
        String[] array = path.replaceFirst("/", "").split("/");
        Map<String, Class> groupMap = mRouteMap.get(array[0]);
        if (groupMap == null) {
            groupMap = new HashMap<>();
            groupMap.put(array[1], activity);
            mRouteMap.put(array[0], groupMap);
        } else {
            groupMap.put(array[1], activity);
        }
    }
}
