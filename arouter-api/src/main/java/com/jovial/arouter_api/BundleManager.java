package com.jovial.arouter_api;

import android.content.Context;
import android.os.Bundle;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/24 0024]
 * About Class:[ 路由参数管理类的参数管理]
 */
public class BundleManager {

    private Bundle bundle = new Bundle();

    //是否使用了startActviityforResult()
    private boolean isResult;

    public Bundle getBundle() {
        return bundle;
    }

    public boolean isResult() {
        return isResult;
    }

    /**
     * 对外提供传参方法
     */
    //@NonNull 不允许传null，@Nullable 可以传null
    public BundleManager withString(@NonNull String key, @Nullable String value){
        bundle.putString(key ,value);
        return this;
    }

    //示例代码需要架构师去拓展
    public BundleManager withResultString(@NonNull String key, @Nullable String value){
        bundle.putString(key,value);
        isResult = true;
        return this;
    }

    public BundleManager withBoolean(@NonNull String key, boolean value){
        bundle.putBoolean(key ,value);
        return this;
    }
    public BundleManager withInt(@NonNull String key,  int value){
        bundle.putInt(key ,value);
        return this;
    }
    public BundleManager withBundle(@NonNull Bundle bundle){
        this.bundle = bundle;
        return this;
    }

    //直接跳转startActivity
    public Object navigation(Context context){
        return navigation(context,-1);
    }
    //startActivityforResult,这里的code可能是resultCode，也可以是requestCode,取决于isResult
    public Object navigation(Context context,int code){
        return RouterManager.getInstance().navigation(context,this,code);
    }
}
