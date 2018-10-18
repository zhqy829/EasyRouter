package com.zhqydot.framework.easyrouter.core.filter;

import android.support.annotation.NonNull;

public class FilterWrapper implements Comparable<FilterWrapper> {

    private RouteFilter mFilter;
    private int mPriority;

    FilterWrapper(RouteFilter filter, int priority) {
        this.mFilter = filter;
        this.mPriority = priority;
    }

    public RouteFilter getFilter() {
        return mFilter;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof FilterWrapper)) {
            return false;
        }
        FilterWrapper fw = (FilterWrapper) obj;
        return mFilter == fw.mFilter;
    }

    @Override
    public int hashCode() {
        return mFilter.hashCode();
    }

    @Override
    public int compareTo(@NonNull FilterWrapper o) {
        if (mPriority > o.mPriority) {
            return -1;
        } else if (mPriority < o.mPriority) {
            return 1;
        }
        return 0;
    }
}
