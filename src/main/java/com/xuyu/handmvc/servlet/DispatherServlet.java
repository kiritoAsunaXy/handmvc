package com.xuyu.handmvc.servlet;

import com.xuyu.handmvc.Controller.UserController;
import com.xuyu.handmvc.annoations.XuyuController;
import com.xuyu.handmvc.annoations.XuyuQualifiter;
import com.xuyu.handmvc.annoations.XuyuRequestMapping;
import com.xuyu.handmvc.annoations.XuyuService;
import com.xuyu.handmvc.hand.HandTools;
import com.xuyu.handmvc.hand.HandToolsService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DispatherServlet extends HttpServlet {

    List<String> classNames = new ArrayList<>();
    //IOC容器
    Map<String,Object> beans = new HashMap<>();
    //url映射地址
    Map<String,Object> handMap = new HashMap<>();

    public DispatherServlet() {
        System.out.println("servlet实例启动");
    }

    @Override
    public void init() throws ServletException {
        System.out.println(this.getClass().getClassLoader().getResource(""));
        //扫描哪些类要被实例化
        doScanPackage("com/xuyu");
        for(String cname:classNames){
            System.out.println(cname);
        }
        //已经获得所有类的全类名路径
        diInstance();

        //依赖注入
        iocDi();

        //建立一个URL与method的映射关系
        xuyuHandMapper();
        for(Map.Entry<String,Object> entry : handMap.entrySet()){
            System.out.println(entry.getKey()+" : "+entry.getValue());
        }
    }

    //建立映射关系
    private void xuyuHandMapper() {

        if(beans.entrySet().size() <= 0){
            System.out.println("类没有被实例化");
            return;
        }
        for(Map.Entry<String,Object> entry : beans.entrySet()){
            Object instance = entry.getValue();
            Class clazz = instance.getClass();
            if(clazz.isAnnotationPresent(XuyuController.class)){
                XuyuRequestMapping requestMapping = (XuyuRequestMapping) clazz.getAnnotation(XuyuRequestMapping.class);
                if(requestMapping != null){
                    String classPath = requestMapping.value();
                    //拿到所有的方法
                    Method[] methods = clazz.getMethods();
                    for(Method method : methods){
                        if(method.isAnnotationPresent(XuyuRequestMapping.class)){
                            XuyuRequestMapping mapping = method.getAnnotation(XuyuRequestMapping.class);
                            String methodUrl = mapping.value();
                            handMap.put(classPath+methodUrl,method);
                        }else {
                            continue;
                        }
                    }
                }
            }
        }
    }

    //依赖注入
    private void iocDi() {
        if(beans.entrySet().size() <= 0){
            System.out.println("类没有被实例化");
            return;
        }
        //把service注入controller,这里需要注意java中对于map的遍历
        for(Map.Entry<String,Object> entry : beans.entrySet()){
            Object instance = entry.getValue();
            //获取类里面声明了哪些注解
            Class clazz = instance.getClass();
            if(clazz.isAnnotationPresent(XuyuController.class)){
                //java.lang.reflect.Field 为我们提供了获取当前对象的成员变量的类型，和重新设值的方法。
                Field[] fileds = clazz.getDeclaredFields();
                for(Field field : fileds){
                    if(field.isAnnotationPresent(XuyuQualifiter.class)){
                        XuyuQualifiter qualifiter = field.getAnnotation(XuyuQualifiter.class);
                        String value = qualifiter.value();
                        //放开权限，因为service是Private的,受保护的
                        field.setAccessible(true);

                        try {
                            field.set(instance,beans.get(value));
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }else {
                        continue;
                    }
                }
            }else {
                continue;
            }
        }

    }


    //实例化所有类
    private void diInstance() {
        if(classNames.size()<=0){
            System.out.println("doScanFailed........");
            return;
        }

        //遍历扫描到的，通过反射创建对象
        for(String className : classNames){
            String cn = className.replace(".class","").replace("/",".");


            try {
                Class clazz = Class.forName(cn);
                if(clazz.isAnnotationPresent(XuyuController.class)){
                   // Annotation controller = clazz.getAnnotation(XuyuController.class);
                    Object instance = clazz.newInstance(); //拿到实例化的bean
                    XuyuRequestMapping requestMapping = (XuyuRequestMapping) clazz.getAnnotation(XuyuRequestMapping.class);
                    String key = requestMapping.value();
                    beans.put(key,instance);
                }else if(clazz.isAnnotationPresent(XuyuService.class)){
                    Object instance = clazz.newInstance(); //拿到实例化的bean
                    XuyuService servicename = (XuyuService) clazz.getAnnotation(XuyuService.class);
                    String key = servicename.value();
                    beans.put(key,instance);
                }else {
                    continue;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    //扫描class类
    private void doScanPackage(String basePackage) {
        System.out.println("进入执行");
        //扫描编译好的项目路径下的所有类
        URL url = this.getClass().getClassLoader().getResource(basePackage);
        String fileStr = url.getFile();
        File file = new File(fileStr);

        String[] files = file.list();

        for(String path : files){
            File filePath = new File(fileStr+"/"+path);//找到我们的磁盘路径+com.xuyu....
            if(filePath.isDirectory()){
                doScanPackage(basePackage+"/"+path);
            }else {
                classNames.add(basePackage+"/"+filePath.getName()); //得到类名
            }
        }

    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
         this.doPost(req,resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //发送的请求会来这执行，是请求的入口,http://localhost:8080/xuyu/aa
        String url = req.getRequestURI(); //  /xuyu/aa
        String context = req.getContextPath();//拿到项目名,springboot下不需要, " "
        String path = url.replaceAll(context,"");
        Method method = (Method) handMap.get(path);

        //现在拿到了方法，要把参数拿到
        UserController instance = (UserController) beans.get("/"+path.split("/")[1]);


        HandTools hand = (HandTools) beans.get("xuyuHandTool");
        //处理器，使用策略模式
        Object[] args =hand.hand(req,resp,method,beans);
        try {
            method.invoke(instance,args);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }


    }
}
