package com.jovial.moduleprojectdemo.base;

import android.app.Application;

import com.jovial.library.utils.RecordPathManager;
import com.jovial.moduleprojectdemo.MainActivity;
import com.jovial.order.Order_MainActivity;
import com.jovial.personal.Personal_MainActivity;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/19 0019]
 * About Class:[ ]
 */
public class BaseApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        /**
         * 使用APT技术就不需要在这里join了，会自动生成java类文件给开发者调用
         */
        /*在这里把所有Activity加入到全局的路径管理器中*/
        RecordPathManager.joinGroup("app","MainActivity", MainActivity.class);
        RecordPathManager.joinGroup("order","Order_MainActivity", Order_MainActivity.class);
        RecordPathManager.joinGroup("personal","Personal_MainActivity", Personal_MainActivity.class);
    }
}
