package com.jovial.compiler.utils;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/20 0020]
 * About Class:[
 * 常量类
 *
 * 在注解处理器接收参数的时候使用常量
 * ]
 */
public class Constants {
    //注解处理器支持的注解类型
    public static final String AROUTER_ANNOTATION_TYPES = "com.jovial.annotation.ARouter";
    //参数注解器支持的注解类型
    public static final String PARAMETER__ANNOTATION_TYPES = "com.jovial.annotation.Parameter";

    //每个子模块的模块名
    public static final String MODULE_NAME = "moduleName";
    //用于存放APT生成的类文件
    public static final String APT_PACKAGE = "packageNameForAPT";

    //String的全类名
    public static final String STRING = "java.lang.String";
    //Activity全类名  为什么要用常量，英文这里是java项目，是拿不到activity的，下面同样的原理
    public static final String ACTIVITY = "android.app.Activity";

    //包名前缀封装
    public static final String BASE_PACKAGE = "com.jovial.arouter_api";
    //路由组 Group加载接口
    public static final String AROUTER_GROUP = BASE_PACKAGE + ".core.ARouterLoadGroup";
    //路由组Group对应的详细path加载接口
    public static final String AROUTER_PATH = BASE_PACKAGE + ".core.ARouterLoadPath";
    //参数加载接口
    public static final String PARAMETER_LOAD = BASE_PACKAGE + ".core.ParameterLoad";
    //跨模块业务，回调接口
    public static final String CALL = BASE_PACKAGE + ".core.Call";
    public static final String ROUTER_MANAGER = "RouterManager";


    //luyou组Group对应的详细Path，的方法名
    public static final String PATH_METHOD_NAME = "loadPath";
    //luyou组Group的方法名
    public static final String GROUP_METHOD_NAME = "loadGroup";
    //获取参数，参数名
    public static final String PARAMETER_METHOD_NAME = "loadParameter";
    //路由组Group，方法名
    public static final String PARAMETER_NAME = "target";
    //路由组Group对应的详细Path，参数名
    public static final String PATH_PARAMETER_NAME = "pathMap";
    //路由组Group参数名
    public static final String GROUP_PARAMETER_NAME = "groupMap";

    //APT生成的路由组Group对应的详细Path类 文件名
    public static final String PATH_FILE_NAME = "ARouter$$Path$$";
    //APT生成的路由组Group对应的详细Group类 文件名
    public static final String GROUP_FILE_NAME = "ARouter$$Group$$";
    //APT生成的获取参数类文件名
    public static final String PARAMETER_FILE_NAME = "$$Parameter";














}
