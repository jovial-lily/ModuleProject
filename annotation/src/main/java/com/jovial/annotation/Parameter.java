package com.jovial.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/24 0024]
 * About Class:[ 属性注解]
 */
@Target(ElementType.FIELD) //注解作用在属性之上
public @interface Parameter {
    //不填写name的注解值表示该属性名就是key，填写了就用注解值作为key
    //从getIntent()方法中获取传递参数值
    String name() default "";
}
