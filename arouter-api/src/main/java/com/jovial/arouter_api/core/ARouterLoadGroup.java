package com.jovial.arouter_api.core;

import java.util.Map;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/20 0020]
 * About Class:[
 * 路由组Group对外提供加载数据接口
 * ]
 */
public interface ARouterLoadGroup {
    /**
     * 加载路由组group数据
     * 比如："app",ARouter$$Path$$app.class(实现了ARouterLoadPath接口)
     * @return key:"app",value:"app"分组对应的路由详细对象类
     */
    Map<String,Class<? extends ARouterLoadPath>> loadGroup();
}
