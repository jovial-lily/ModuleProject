package com.jovial.arouter_api.core;

import com.jovial.annotation.model.RouterBean;

import java.util.Map;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/20 0020]
 * About Class:[
 * 路由组Group对应的信息Path加载数据接口
 * 比如：app分组对应有哪些类需要加载
 * ]
 */
public interface ARouterLoadPath {
    /**
     * 加载路由组Group中的Path详细数据
     * 比如："app"分组下有这些信息：
     * @return key:"/app/MainActivity",value:MainActivity信息封装到RouterBean对象中
     */
    Map<String, RouterBean> loadPath();
}
