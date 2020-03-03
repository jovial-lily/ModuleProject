package com.jovial.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/19 0019]
 * About Class:[ ]
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.CLASS)
public @interface ARouter {

    //详细路由路径（必填），如："/app/MainActivity"
    String path();

    //从path中截取出来   一定要规范开发者的编码
    String group() default "";
}
