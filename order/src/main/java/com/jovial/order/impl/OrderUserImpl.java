package com.jovial.order.impl;

import com.jovial.annotation.ARouter;
import com.jovial.library.user.BaseUser;
import com.jovial.library.user.IUser;
import com.jovial.order.model.UserInfo;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/25 0025]
 * About Class:[ ]
 */
@ARouter(path = "/order/getUserInfo")
public class OrderUserImpl implements IUser {
    @Override
    public BaseUser getUserInfo() {
        UserInfo userInfo = new UserInfo();
        userInfo.setName("jovial");
        userInfo.setAccount("WL_j");
        userInfo.setPassword("163666");
        userInfo.setVipLevel(9);
        return userInfo;
    }
}
