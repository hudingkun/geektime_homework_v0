package org.china.bang.web.mvc;

import org.apache.commons.lang.StringUtils;
import org.china.bang.web.mvc.controller.Controller;
import org.china.bang.web.mvc.controller.PageController;
import org.china.bang.web.mvc.controller.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

import static org.apache.commons.lang.StringUtils.substringAfter;

public class FrontControllerServlet extends HttpServlet {

    /**
     * 请求路径 和 Controller 的映射关系缓存
     */
    private Map<String, Controller> controllerMapping = new HashMap<>();


    /**
     * 请求路径 和 {@link HandlerMethodInfo} 的映射关系缓存
     */
    private Map<String,HandlerMethodInfo> handlerMethodInfoMapping = new HashMap<>();

    @Override
//    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        String requestURI = req.getRequestURI();
//        String contextPath = req.getContextPath();
//        String prefixPath = contextPath;
//        String requestMappingPath = StringUtils.substringAfter(requestURI, StringUtils.replace(prefixPath, "//", "/"));
//
//        Controller controller = controllerMapping.get(requestMappingPath);
//        if(Objects.isNull(controller))
//        {
//           return;
//        }
//
//        HandlerMethodInfo handlerMethodInfo = handlerMethodInfoMapping.get(requestMappingPath);
//        if(Objects.isNull(handlerMethodInfo))
//        {
//            return;
//        }
//
//        if(!handlerMethodInfo.getSupportedHttpMethods().contains(req.getMethod())){
//            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
//            return;
//        }
//
//        try {
//            if (controller instanceof PageController) {
//                PageController pageController = PageController.class.cast(controller);
//                String viewPath = pageController.execute(req, resp);
//
//                //方式一： 通过请求转发器，将请求转发到对应的JSP页面
//               // req.getRequestDispatcher(viewPath).forward(req,resp);
//
//                //方式二： 通过ServletContext转发器，将请求转发到对应的JSP页面
//                if(!viewPath.startsWith("/")){
//                    viewPath = "/" + viewPath;
//                }
//                req.getServletContext().getRequestDispatcher(viewPath).forward(req,resp);
//                return;
//
//            } else if (controller instanceof RestController) {
//
//            }
//        } catch (Throwable throwable) {
//            if(throwable.getCause() instanceof IOException)
//            {
//                throw (IOException) throwable.getCause();
//            }else{
//                throw new ServletException(throwable.getMessage());
//            }
//        }
//    }

    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // 建立映射关系
        // requestURI = /a/hello/world
        String requestURI = request.getRequestURI();
        // contextPath  = /a or "/" or ""
        String servletContextPath = request.getContextPath();
        String prefixPath = servletContextPath;
        // 映射路径（子路径）
        String requestMappingPath = substringAfter(requestURI,
                StringUtils.replace(prefixPath, "//", "/"));
        // 映射到 Controller
        Controller controller = controllerMapping.get(requestMappingPath);

        if (controller != null) {

            HandlerMethodInfo handlerMethodInfo = handlerMethodInfoMapping.get(requestMappingPath);

            try {
                if (handlerMethodInfo != null) {

                    String httpMethod = request.getMethod();

                    if (!handlerMethodInfo.getSupportedHttpMethods().contains(httpMethod)) {
                        // HTTP 方法不支持
                        response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                        return;
                    }

                    if (controller instanceof PageController) {
                        PageController pageController = PageController.class.cast(controller);
                        String viewPath = pageController.execute(request, response);
                        // 页面请求 forward
                        // request -> RequestDispatcher forward
                        // RequestDispatcher requestDispatcher = request.getRequestDispatcher(viewPath);
                        // ServletContext -> RequestDispatcher forward
                        // ServletContext -> RequestDispatcher 必须以 "/" 开头
                        ServletContext servletContext = request.getServletContext();
                        if (!viewPath.startsWith("/")) {
                            viewPath = "/" + viewPath;
                        }
                        RequestDispatcher requestDispatcher = servletContext.getRequestDispatcher(viewPath);
                        requestDispatcher.forward(request, response);
                        return;
                    } else if (controller instanceof RestController) {
                        // TODO
                    }

                }
            } catch (Throwable throwable) {
                if (throwable.getCause() instanceof IOException) {
                    throw (IOException) throwable.getCause();
                } else {
                    throw new ServletException(throwable.getCause());
                }
            }
        }
    }

    @Override
    public void destroy() {
        controllerMapping.clear();
        handlerMethodInfoMapping.clear();
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        initHandlerMethods();
    }

    private void initHandlerMethods() {
        for (Controller controller : ServiceLoader.load(Controller.class)) {
            Class<? extends Controller> controllerClass = controller.getClass();
            Path pathFromClass = controllerClass.getAnnotation(Path.class);
            String requestPath = pathFromClass.value();
            Method[] publicMethods = controllerClass.getMethods();
            for (Method method : publicMethods) {
               Set<String> supportedHttpMethods = findSupportedHttpMethods(method);
                Path pathFromMethod = method.getAnnotation(Path.class);
                if (Objects.nonNull(pathFromMethod)) {
                    requestPath += pathFromMethod.value();
                }
                handlerMethodInfoMapping.put(requestPath,new HandlerMethodInfo(requestPath,method,supportedHttpMethods));
            }
            controllerMapping.put(requestPath,controller);
        }

    }

    private Set<String> findSupportedHttpMethods(Method method) {

        Set<String> supportedHttpMethods = new HashSet<>();
        for (Annotation annotationFromMethod : method.getAnnotations()) {
            HttpMethod annotation = annotationFromMethod.annotationType().getAnnotation(HttpMethod.class);
            if (Objects.nonNull(annotation)) {
                supportedHttpMethods.add(annotation.value());
            }
        }

        if (supportedHttpMethods.isEmpty()) {
            supportedHttpMethods.addAll(Arrays.asList(
                    HttpMethod.GET, HttpMethod.POST,
                    HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS));
        }

        return supportedHttpMethods;
    }
}
