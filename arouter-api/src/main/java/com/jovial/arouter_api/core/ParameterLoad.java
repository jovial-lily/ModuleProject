package com.jovial.arouter_api.core;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/24 0024]
 * About Class:[ 参数Parameter加载接口]
 */
public interface ParameterLoad {
    /**
     * 目标对象，属性名=getIntent().属性类型（"注解值or属性名"）;完成赋值
     * @param target 目标对象，如：MainActivity（中的某些属性）
     */
    void loadParameter(Object target);
}
