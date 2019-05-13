package com.xuyu.handmvc.Controller;

import com.xuyu.handmvc.annoations.*;
import com.xuyu.handmvc.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@XuyuController
@XuyuRequestMapping("/xuyu")
public class UserController {

    @XuyuQualifiter("xuyuServiceImpl")
    private UserServiceImpl userService;


    @XuyuRequestMapping("/query")
    public void  query(HttpServletRequest request, HttpServletResponse response
            ,@XuyuRequestParam("name") String name
            ,@XuyuRequestParam("age") Integer age) throws IOException {

        PrintWriter pw = response.getWriter();
        String result = userService.query(name, age);
        pw.write(result);

    }
}
