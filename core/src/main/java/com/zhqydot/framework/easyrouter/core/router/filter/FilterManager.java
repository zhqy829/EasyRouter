package com.zhqydot.framework.easyrouter.core.router.filter;

import android.os.Bundle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FilterManager {

    private Set<FilterWrapper> globalFilters = new HashSet<>();
    private Map<String, Set<FilterWrapper>> groupFilterMap = new HashMap<>();

    public void addFilter(RouteFilter filter, int priority) {
        if (filter != null) {
            globalFilters.add(new FilterWrapper(filter, priority));
        }
    }

    public void removeFilter(RouteFilter filter) {
        FilterWrapper deleteFilterWrapper = null;
        for (FilterWrapper wrapper : globalFilters) {
            if (wrapper.filter == filter) {
                deleteFilterWrapper = wrapper;
                break;
            }
        }
        globalFilters.remove(deleteFilterWrapper);
    }

    public void addFilter(RouteFilter filter, String group, int priority) {
        if (filter != null) {
            Set<FilterWrapper> groupFilters = groupFilterMap.get(group);
            if (groupFilters == null) {
                groupFilters = new HashSet<>();
                groupFilterMap.put(group, groupFilters);
            }
            groupFilters.add(new FilterWrapper(filter, priority));
        }
    }

    public void removeFilter(RouteFilter filter, String group) {
        Set<FilterWrapper> groupFilters = groupFilterMap.get(group);
        if (groupFilters != null) {
            FilterWrapper deleteFilterWrapper = null;
            for (FilterWrapper wrapper : groupFilters) {
                if (wrapper.filter == filter) {
                    deleteFilterWrapper = wrapper;
                    break;
                }
            }
            groupFilters.remove(deleteFilterWrapper);
            if (groupFilters.size() == 0) {
                groupFilterMap.remove(group);
            }
        }
    }

    public boolean doFilter(String path, Bundle bundle) {
        for (FilterWrapper wrapper : globalFilters) {
            boolean isFilter = wrapper.filter.onRoute(path, bundle);
            if (!isFilter) {
                return false;
            }
        }
        String group = path.replaceFirst("/", "").split("/")[0];
        Set<FilterWrapper> groupFilters = groupFilterMap.get(group);
        if (groupFilters != null) {
            for (FilterWrapper wrapper : groupFilters) {
                boolean isFilter = wrapper.filter.onRoute(path, bundle);
                if (!isFilter) {
                    return false;
                }
            }
        }
        return true;
    }

}
