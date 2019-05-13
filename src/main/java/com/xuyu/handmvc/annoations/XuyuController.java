package com.xuyu.handmvc.annoations;


import java.lang.annotation.*;

@Target({ElementType.TYPE}) //作用在类上的
@Retention(RetentionPolicy.RUNTIME)//表示生命周期，在运行期间能够通过反射获得
@Documented
public @interface XuyuController {
    String value() default "";  //代表@Controller("value")
}
