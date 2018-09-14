package com.zhqydot.framework.easyrouter.core.router;

public class RouteException extends RuntimeException {
    public RouteException() {
        super("Unable to find an activity for the path, have you register before?");
    }
}
