package com.jovial.moduleprojectdemo.test;

import com.jovial.arouter_api.core.ARouterLoadGroup;
import com.jovial.arouter_api.core.ARouterLoadPath;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/21 0021]
 *
 * About Class:[ 模拟路由器的组文件，最终由APT注解处理类生成的组的模板]
 *
 * 这里是：一对一，在集成化后所有被ARouter标记过的模块都会 生成两个类
 * 这是第一个类，是一对一的关系保存这path类，而path类就是一对多的关系
 * path类就会把模块里面所有被ARouter注解的类都添加到map集合中去
 */
public class ARouter$$Group$$order implements ARouterLoadGroup {
    @Override
    public Map<String, Class<? extends ARouterLoadPath>> loadGroup() {
        Map<String, Class<? extends ARouterLoadPath>> groupMap = new HashMap<>();
        groupMap.put("order",ARouter$$Path$$order.class);
        return groupMap;
    }
}
