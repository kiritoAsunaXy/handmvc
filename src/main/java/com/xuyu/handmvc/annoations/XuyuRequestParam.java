package com.xuyu.handmvc.annoations;


import java.lang.annotation.*;

@Target({ElementType.PARAMETER}) //作用参数
@Retention(RetentionPolicy.RUNTIME)//表示生命周期，在运行期间能够通过反射获得
@Documented
public @interface XuyuRequestParam {
    String value() default "";  //代表@Qualifiter("value")的细粒度注入
}
