package com.jovial.order.model;

import com.jovial.library.user.BaseUser;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/25 0025]
 * About Class:[ ]
 */
public class UserInfo extends BaseUser {
    private String token;
    private int vipLevel;

    public String getToken(){return token;}

    public void setToken(String token){
        this.token = token;
    }

    public int getVipLevel(){
        return vipLevel;
    }

    public void setVipLevel(int vipLevel) {
        this.vipLevel = vipLevel;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "name='" + getName() + '\'' +
                "token='" + getToken() + '\'' +
                "vipLevel='" + getVipLevel() + '\'' +
                ", account='" + getAccount() + '\'' +
                ", password='" + getPassword() + '\'' +
                '}';
    }
}
