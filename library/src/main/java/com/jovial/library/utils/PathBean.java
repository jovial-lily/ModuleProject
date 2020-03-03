package com.jovial.library.utils;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/19 0019]
 * About Class:[ 已经被路由方式代替，并且有一个新的升级版的Bean]
 */
public class PathBean {
    private String path;
    private Class<?> clazz;

    public PathBean(String path,Class<?> clazz){
        this.path = path;
        this.clazz = clazz;
    }
    public String getPath() {
        return path;
    }

    public Class<?> getClazz() {
        return clazz;
    }
}
