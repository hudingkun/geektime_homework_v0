package com.china.bang.projects.user.web.controller;

import com.china.bang.projects.user.domain.User;
import com.china.bang.projects.user.service.UserService;
import com.china.bang.projects.user.service.impl.UserServiceImpl;
import org.china.bang.web.mvc.controller.PageController;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

@Path("/user")
public class HelloWorldController implements PageController {

    @POST
    @GET
    @Path("/register")
    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Throwable {

        ServletContext servletContext = request.getServletContext();
        try {

            User user = new User();
            user.setName("hdk2");
            user.setPassword("hdk2");
            user.setEmail("11@11.com");
            user.setPhoneNumber("11111");

            UserService userService = new UserServiceImpl();
            userService.register(user);

        }catch (Exception e)
        {
            servletContext.log("当前编码已设置为：" + e.getStackTrace());
        }

        return "index.jsp";
    }


}
