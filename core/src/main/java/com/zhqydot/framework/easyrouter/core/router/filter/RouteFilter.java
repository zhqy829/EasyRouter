package com.zhqydot.framework.easyrouter.core.router.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;

public interface RouteFilter {
    boolean onRoute(String path, @Nullable Bundle data);
}
