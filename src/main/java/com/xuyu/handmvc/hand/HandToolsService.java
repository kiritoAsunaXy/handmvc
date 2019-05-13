package com.xuyu.handmvc.hand;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;

public interface HandToolsService {

    //方法入口，根据参数判断具体的实现
    public  Object[] hand(HttpServletRequest request, HttpServletResponse response, Method method, Map<String, Object> map);

}
