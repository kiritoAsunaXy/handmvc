package com.xuyu.handmvc.argumentResolve;

import com.xuyu.handmvc.annoations.XuyuService;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@XuyuService("HttpServletResponseArgResolver")
public class HttpServletResponseArgResolver implements ArgumentResolve{

    @Override
    public boolean support(Class type, int index, Method method) {
        return ServletResponse.class.isAssignableFrom(type);
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class type, int index, Method method) {
        return response;
    }
}
