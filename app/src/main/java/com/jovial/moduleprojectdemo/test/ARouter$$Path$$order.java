package com.jovial.moduleprojectdemo.test;

import com.jovial.annotation.model.RouterBean;
import com.jovial.arouter_api.core.ARouterLoadPath;
import com.jovial.order.Order_MainActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/21 0021]
 * About Class:[模拟路由器的路径文件， 最终由APT注解处理类生成的路径]
 * 这是第二个类，是一对多的关系，就会把模块里面所有被ARouter注解的类都添加到map集合中去
 */
public class ARouter$$Path$$order implements ARouterLoadPath {
    @Override
    public Map<String, RouterBean> loadPath() {
        Map<String, RouterBean> pathMap = new HashMap<>();
        //把被注解的类添加到map
        RouterBean rb = RouterBean.create(RouterBean.Type.ACTIVITY,
                Order_MainActivity.class,
                "/order/Order_MainActivity",
                "order");
        pathMap.put("/order/Order_MainActivity",rb);

        //这里还会有很多，比如OrderDetialActivity,OrderDessActvity.....
//        RouterBean rb2 = RouterBean.create(RouterBean.Type.ACTIVITY,
//                OrderDetialActivity.class,
//                "/order/OrderDetialActivity",
//                "order");
//        pathMap.put("/order/OrderDetialActivity",rb2);

        return pathMap;
    }









}
