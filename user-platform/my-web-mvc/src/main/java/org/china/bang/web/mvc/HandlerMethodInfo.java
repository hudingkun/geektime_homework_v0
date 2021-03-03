package org.china.bang.web.mvc;

import java.lang.reflect.Method;
import java.util.Set;

public class HandlerMethodInfo {

    private final String requestPath;

    private final Method handlerMethod;

    private final Set<String> supportedHttpMethods;

    public HandlerMethodInfo(String requestPath, Method method, Set<String> supportedHttpMethods) {
        this.requestPath = requestPath;
        this.handlerMethod = method;
        this.supportedHttpMethods = supportedHttpMethods;
    }

    public String getRequestPath() {
        return requestPath;
    }

    public Method getHandlerMethod() {
        return handlerMethod;
    }

    public Set<String> getSupportedHttpMethods() {
        return supportedHttpMethods;
    }
}
