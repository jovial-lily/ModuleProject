package com.jovial.order.impl;

import com.jovial.annotation.ARouter;
import com.jovial.library.order.OrderDrawable;
import com.jovial.order.R;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/25 0025]
 * About Class:[ 实现了公共库的接口，提供给外部模块访问本模块的资源]
 */
@ARouter(path = "/order/getDrawable")
public class OrderDrawableImpl implements OrderDrawable {
    @Override
    public int getDrawable() {

        return R.drawable.icon_investm_up;
    }
}
