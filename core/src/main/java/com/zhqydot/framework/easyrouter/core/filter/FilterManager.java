package com.zhqydot.framework.easyrouter.core.filter;

import android.os.Bundle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FilterManager {

    private Set<FilterWrapper> mGlobalFilters = new HashSet<>();
    private Map<String, Set<FilterWrapper>> mGroupFilterMap = new HashMap<>();

    public void addFilter(RouteFilter filter, int priority) {
        if (filter != null) {
            mGlobalFilters.add(new FilterWrapper(filter, priority));
        }
    }

    public void removeFilter(RouteFilter filter) {
        FilterWrapper deleteFilterWrapper = null;
        for (FilterWrapper wrapper : mGlobalFilters) {
            if (wrapper.getFilter() == filter) {
                deleteFilterWrapper = wrapper;
                break;
            }
        }
        mGlobalFilters.remove(deleteFilterWrapper);
    }

    public void addFilter(RouteFilter filter, String group, int priority) {
        if (filter != null) {
            Set<FilterWrapper> groupFilters = mGroupFilterMap.get(group);
            if (groupFilters == null) {
                groupFilters = new HashSet<>();
                mGroupFilterMap.put(group, groupFilters);
            }
            groupFilters.add(new FilterWrapper(filter, priority));
        }
    }

    public void removeFilter(RouteFilter filter, String group) {
        Set<FilterWrapper> groupFilters = mGroupFilterMap.get(group);
        if (groupFilters != null) {
            FilterWrapper deleteFilterWrapper = null;
            for (FilterWrapper wrapper : groupFilters) {
                if (wrapper.getFilter() == filter) {
                    deleteFilterWrapper = wrapper;
                    break;
                }
            }
            groupFilters.remove(deleteFilterWrapper);
            if (groupFilters.size() == 0) {
                mGroupFilterMap.remove(group);
            }
        }
    }

    public boolean doFilter(String path, Bundle bundle) {
        for (FilterWrapper wrapper : mGlobalFilters) {
            boolean isFilter = wrapper.getFilter().onRoute(path, bundle);
            if (!isFilter) {
                return false;
            }
        }
        String group = path.replaceFirst("/", "").split("/")[0];
        Set<FilterWrapper> groupFilters = mGroupFilterMap.get(group);
        if (groupFilters != null) {
            for (FilterWrapper wrapper : groupFilters) {
                boolean isFilter = wrapper.getFilter().onRoute(path, bundle);
                if (!isFilter) {
                    return false;
                }
            }
        }
        return true;
    }

}
