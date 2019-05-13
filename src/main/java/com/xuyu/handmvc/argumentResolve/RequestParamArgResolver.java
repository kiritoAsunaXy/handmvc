package com.xuyu.handmvc.argumentResolve;

import com.xuyu.handmvc.annoations.XuyuRequestParam;
import com.xuyu.handmvc.annoations.XuyuService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@XuyuService("RequestParamArgResolver")
public class RequestParamArgResolver implements ArgumentResolve{

    @Override
    public boolean support(Class type, int index, Method method) {
        Annotation[][] annotation = method.getParameterAnnotations();
        Annotation[] paramAnnos = annotation[index];
        for(Annotation an : paramAnnos){
            if(XuyuRequestParam.class.isAssignableFrom(an.getClass())){
                return true;
            }
        }

        return false;
    }

    @Override
    public Object argumentResolver(HttpServletRequest request, HttpServletResponse response, Class type, int index, Method method) {
        Annotation[][] annotation = method.getParameterAnnotations();
        Annotation[] paramAnnos = annotation[index];
        for(Annotation an : paramAnnos){
            if(XuyuRequestParam.class.isAssignableFrom(an.getClass())){

                XuyuRequestParam xp = (XuyuRequestParam) an;
                String value = xp.value(); //比如传了name,从request中取出
                if(type.isAssignableFrom(Integer.class)){
                    return  Integer.parseInt(request.getParameter(value));
                }else {
                    return request.getParameter(value);
                }

            }
        }
        return null;
    }
}
