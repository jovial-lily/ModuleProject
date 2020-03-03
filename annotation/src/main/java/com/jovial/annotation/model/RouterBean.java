package com.jovial.annotation.model;

import javax.lang.model.element.Element;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/20 0020]
 * About Class:[
 *  PathBean的升级版
 *  这个类为啥要放到annotation里面，是因为包括annotation在内的所有子模块都需要引用这个类
 *  所以写到这里是比较合适的
 *
 * ]
 */
public class RouterBean {

    public enum Type{

        ACTIVITY,
        //跨模块的业务
        CALL
    }

    public Type getType() {
        return type;
    }

    //枚举类型
    private Type type;
    //类节点
    private Element element;

    public Element getElement() {
        return element;
    }

    //被@ARouter注解的类对象
    private Class<?> clazz;
    //路由的组名
    private String group;
    //路由的地址
    private String path;


    /**
     * 私有化构造函数，外界无法通过new的方式，只能通过build来构建对象
     * @param builder 构建对象的
     */
    private RouterBean(Builder builder){
        this.element = builder.element;
        this.path = builder.path;
        this.group = builder.group;
    }
    private RouterBean(Type type, Class<?> clazz, String path, String group) {
        this.type = type;
        this.clazz = clazz;
        this.path = path;
        this.group = group;
    }
    /**
     * 对外还提供一种简单的实例化方法
     */
    public static RouterBean create(Type type,Class<?> clazz,String path,String group){
        return new RouterBean(type,clazz,path,group);
    }

    /**
     * 构建者模式
     */
    public final static class Builder{
        //类节点
        private Element element;
        //路由的组名
        private String group;
        //路由的地址
        private String path;

        public Builder setElement(Element element) {
            this.element = element;
            return this;
        }

        public Builder setGroup(String group) {
            this.group = group;
            return this;
        }

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        //最后的build方法或者create方法往往是做参数的校验或者初始化
        public RouterBean build(){
            if(path == null || path.length() == 0){
                throw new IllegalArgumentException("path必填项为空：如：/app/MainActivity");
            }
            return new RouterBean(this);
        }
    }

    @Override
    public String toString() {
        return "RouterBean{" +
                "type=" + type +
                ", element=" + element +
                ", clazz=" + clazz +
                ", group='" + group + '\'' +
                ", path='" + path + '\'' +
                '}';
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Class<?> getClazz() {
        return clazz;
    }


    public void setType(Type type) {
        this.type = type;
    }





}
