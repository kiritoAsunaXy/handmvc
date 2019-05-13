package com.xuyu.handmvc.argumentResolve;

import com.xuyu.handmvc.annoations.XuyuService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@XuyuService("HttpServletRequetArgResolver")
public class HttpServletRequetArgResolver implements ArgumentResolve{

    @Override
    public boolean support(Class type, int index, Method method) {
        return ServletRequest.class.isAssignableFrom(type);
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class type, int index, Method method) {
        return request;
    }
}
