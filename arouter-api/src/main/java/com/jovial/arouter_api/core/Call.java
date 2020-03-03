package com.jovial.arouter_api.core;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/25 0025]
 * About Class:[
 * 跨模块业务的接口 ，
 * 需要提供给其他模块用的任意资源都需要继承这个接口
 * 并且在自己的模块中实现继承了这个接口的接口类。
 * 在需要使用的模块中使用@Parameter（path）中通过path找到并且通过接口引用。
 * ]
 */
public interface Call {
}
