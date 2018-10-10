package com.zhqydot.framework.easyrouter.core.router.filter;

import android.support.annotation.NonNull;

public class FilterWrapper implements Comparable<FilterWrapper> {

    RouteFilter filter;
    int priority;

    FilterWrapper(RouteFilter filter, int priority) {
        this.filter = filter;
        this.priority = priority;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FilterWrapper)) {
            return false;
        }
        FilterWrapper fw = (FilterWrapper) obj;
        return filter == fw.filter;
    }

    @Override
    public int hashCode() {
        return filter.hashCode();
    }

    @Override
    public int compareTo(@NonNull FilterWrapper o) {
        if (priority > o.priority) {
            return 1;
        } else if (priority < o.priority) {
            return -1;
        }
        return 0;
    }
}
