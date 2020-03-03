package com.jovial.order;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jovial.annotation.ARouter;
import com.jovial.annotation.Parameter;
import com.jovial.arouter_api.ParameterManager;
import com.jovial.arouter_api.RouterManager;
import com.jovial.library.utils.RecordPathManager;
@ARouter( path = "/order/Order_MainActivity")
public class Order_MainActivity extends AppCompatActivity {
    private static final String TAG = Order_MainActivity.class.getName();
    @Parameter
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_main);
        ParameterManager.getInstance().loadParameter(this);
        Log.e(TAG+">>>>>>>>",TAG+"接受到参数："+name);
    }

    /**
     * 跳转到首页
     */
    public void jumpMain(View view) {
        /** 类加载的方式实现 */
//        try {
//            Class targetClass = Class.forName("com.jovial.moduleprojectdemo.MainActivity");
//            Intent intent  = new Intent(this,targetClass);
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
        /** 管理跳转实现 回调 */
        RouterManager.getInstance()
                .build("/app/MainActivity")
                .withResultString("call","i am callBack!")
                .navigation(this);
    }

    /**
     * 全局变量的方式实现跳转
     */
    public void jumpPersonal(View view) {
        Class<?> targetClass = RecordPathManager.getTargetClass("personal", "Personal_MainActivity");
        if(targetClass == null){
            Log.i(">>>","获取类为空");
            return;
        }
        Intent intent  = new Intent(this,targetClass);
        startActivity(intent);
    }
}
