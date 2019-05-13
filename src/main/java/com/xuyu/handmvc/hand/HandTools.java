package com.xuyu.handmvc.hand;

import com.xuyu.handmvc.annoations.XuyuService;
import com.xuyu.handmvc.argumentResolve.ArgumentResolve;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@XuyuService("xuyuHandTool")
public class HandTools implements HandToolsService {

    //返回方法里的所有参数
    @Override
    public Object[] hand(HttpServletRequest request, HttpServletResponse response
            , Method method, Map<String, Object> beans) {

        Class<?>[] param = method.getParameterTypes();
        Object[] args = new Object[param.length];

        //拿到所有实现了ArgumentResolver的实例
        Map<String,Object> argResolvers = getInsstanceType(beans, ArgumentResolve.class);
        int index = 0;
        int i = 0;
        for(Class paramClazz :param){
            //哪个参数对应了哪个解析类，用策略模式
            for(Map.Entry<String,Object> entry : argResolvers.entrySet()){
                ArgumentResolve ar = (ArgumentResolve) entry.getValue();
               if(ar.support(paramClazz,index,method)){
                   args[i++] = ar.argumentResolver(request,response,paramClazz,index,method);
               }
            }
            index++;
        }
        return args;
    }

    public Map<String,Object> getInsstanceType(Map<String,Object> beans ,Class type){
        Map<String,Object> resultBeans = new HashMap<>();
        for(Map.Entry<String,Object> entry : beans.entrySet()){
            Class[] infs = entry.getValue().getClass().getInterfaces();
            if(infs != null && infs.length>0){
                for(Class inf : infs){
                    if(inf.isAssignableFrom(type)){
                        resultBeans.put(entry.getKey(),entry.getValue());
                    }
                }
            }
        }
        return resultBeans;
    }
}
