package com.jovial.personal;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.jovial.annotation.ARouter;
import com.jovial.annotation.Parameter;
import com.jovial.arouter_api.ParameterManager;
import com.jovial.arouter_api.RouterManager;
import com.jovial.arouter_api.core.ParameterLoad;

@ARouter(path = "/personal/Personal_MainActivity")
public class Personal_MainActivity extends AppCompatActivity {
    private static final String TAG = Personal_MainActivity.class.getName();

    @Parameter
    String name;
    @Parameter(name = "ages")
    int age;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.personal_activity_main);
//        ParameterLoad parameter = new Personal_MainActivity$$Parameter();
//        parameter.loadParameter(this);
        //使用全局的管理类,可以使用懒加载的方式，跳转到哪个类就加载哪个类
        ParameterManager.getInstance().loadParameter(this);

        Log.i(">>>>>>>>","name="+name+": age="+age);
    }

    public void jumpMain(View view) {
        try {
            Class targetClass = Class.forName("com.jovial.moduleprojectdemo.MainActivity");
            Intent intent  = new Intent(this,targetClass);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void jumpOrder(View view) {
        /**类加载方式跳转*/
//        try {
//            Class targetClass = Class.forName("com.jovial.order.Order_MainActivity");
//            Intent intent  = new Intent(this,targetClass);
//            startActivity(intent);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }

        /**全局路由管理跳转  普通跳转 没有resultCode 和 requestCode */
//        RouterManager.getInstance()
//                .build("/order/Order_MainActivity")
//                .withString("name","jovial from personal by no code>>>>>>")
//                .navigation(this);


        /**全局路由管理跳转  resultCode跳转 */
//        RouterManager.getInstance()
//                .build("/order/Order_MainActivity")
//                .withResultString("name","jovial")
//                .navigation(this);

        /**全局路由管理跳转  requestCode跳转 */
        RouterManager.getInstance()
                .build("/order/Order_MainActivity")
                .withString("name","jovial")
                .navigation(this,163);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            String name = data.getStringExtra("name");
            Log.i(TAG + ">>>>>>>","onActivityResult:"+name);
        }
    }

}
