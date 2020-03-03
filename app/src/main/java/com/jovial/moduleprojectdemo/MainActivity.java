package com.jovial.moduleprojectdemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.jovial.annotation.ARouter;
import com.jovial.annotation.Parameter;
import com.jovial.annotation.model.RouterBean;
import com.jovial.arouter_api.ParameterManager;
import com.jovial.arouter_api.RouterManager;
import com.jovial.arouter_api.core.ARouterLoadGroup;
import com.jovial.arouter_api.core.ARouterLoadPath;
import com.jovial.library.order.OrderDrawable;
import com.jovial.library.user.IUser;
import com.jovial.moduleprojectdemo.apt.ARouter$$Group$$order;
import com.jovial.moduleprojectdemo.apt.ARouter$$Group$$personal;
import com.jovial.order.Order_MainActivity;
import com.jovial.personal.Personal_MainActivity;

import java.util.Map;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**

 *   模块与模块之间交互传参的方式有以下几种方式：
 *  1 EventBus：EventBean会非常多（一对一基本没一个方法都有一个eventBean），一对多会混乱不堪难以维护
 *  2 反射： 反射技术可以，维护成本较高而且容易出现高版本@hide（9.0）以上限制
 *  3 隐式意图：维护成本还好，比较麻烦，需要维护manifest里面的action
 *  4 BroadCastReceive  需要动态注册（7.0+），需要写很多sendbroadcast
 *  5 类加载：需要准确的写出类的全名，维护成本高很容易出现人为失误
 *
 *
 *   组件化开发，模块交互跳转的方式目前有两种：
 *   1.类加载的方式实现跳转：参考当前Order_MainActivity & Personal_MainActivity
 *   2.全局路径记录的方式实现跳转:参考公共库Library项目中RecordPathManager
 *
 *   如果把1 、2合并即可引出“路由”的概念，路由的核心就是类加载和全局记录的方式实现的
 *   例如阿里巴巴的理由架构：Arouter架构
 *
 * 阿里ARouter架构的缺点：
 * 1.性能：里面使用5个全局Map、一个List来存储数据，并且没有使用缓存
 * 2.兼容：在华为的部分手机，ClassUtils.class这个类是失败的，因为这个类要扫描dex文件的所有指定包名的所有类，
 *  触及到了敏感的classdex文件，厂商是保护的
 * 3.开启了线程去做耗时的存储操作和第2步骤的扫描操作。
 *
 */

@ARouter( path = "/app/MainActivity")//使用注解的方式实现组件交互
public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getName();

    @Parameter(name = "/order/getDrawable")
    OrderDrawable drawable;

    @Parameter(name = "/order/getUserInfo")
    IUser user;

    @Parameter
    String name;
    @Parameter(name = "ages")
    int age = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        name = getIntent().getStringExtra("name");
//        age = getIntent().getIntExtra("age",age);

        ParameterManager.getInstance().loadParameter(this);
        TextView tv = findViewById(R.id.tv_1);
        tv.setBackgroundResource(drawable.getDrawable());

        Log.e(TAG+">>>>>>>","从Order获取到的UserInfo："+user.getUserInfo().toString());
    }

    public void jumpPersonal(View view) {
        Intent intent = new Intent(this, Personal_MainActivity.class);
        intent.putExtra("name","jovial");
        startActivity(intent);
    }

    public void jumpOrder(View view) {
        Intent intent = new Intent(this, Order_MainActivity.class);
        intent.putExtra("name","jovial");
        startActivity(intent);
    }

    public void jumpAPTPersonal(View view) {
//        startActivity(new Intent(this,Personal_MainActivity$$ARouter.findTargetClass("/personal/Personal_MainActivity")));
        //使用注解+javapoet
        ARouterLoadGroup loadGroup = new ARouter$$Group$$personal();
        Map<String, Class<? extends ARouterLoadPath>> groupMap = loadGroup.loadGroup();
        Class<? extends ARouterLoadPath> personalPathClass = groupMap.get("personal");
        try {
            ARouterLoadPath aRouterLoadPath = personalPathClass.newInstance();
            Map<String, RouterBean> pathMap = aRouterLoadPath.loadPath();
            RouterBean routerBean = pathMap.get("/personal/Personal_MainActivity");
            if(routerBean != null){
                Intent intent = new Intent(this,routerBean.getClazz());
                intent.putExtra("name","jovial");
                intent.putExtra("ages",19);
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void jumpAPTOrder(View view) {
//        startActivity(new Intent(this,OrderActivity$$ARouter.findTargetClass("/app/OrderActivity")));
        /** 管理跳转 */
        RouterManager.getInstance()
                .build("/order/Order_MainActivity")
                .withString("name","jovial")
                .navigation(this,163);
    }

    /**
     * 使用apt+javapoet技术   这里面的代码就是路由实现子模块交互的核心技术。
     * @param view
     */
    public void APTandJavapoet(View view) {
        ARouterLoadGroup loadGroup = new ARouter$$Group$$order();
        Map<String, Class<? extends ARouterLoadPath>> groupMap = loadGroup.loadGroup();
        //模拟MainActivity --> Order_MainActivity
        Class<? extends ARouterLoadPath> orderPathClass = groupMap.get("order");
        //使用类加载的技术
        try {
            //要使用这个对象先初始化
            ARouterLoadPath loadPath = orderPathClass.newInstance();
            //得到的是对象的接口，有实现类直接调用loadPath方法得到path的集合
            Map<String, RouterBean> pathMap = loadPath.loadPath();
            //根据路径得到具体保存类信息的实体类
            RouterBean routerBean = pathMap.get("/order/Order_MainActivity");
            if(routerBean != null){
                Intent intent = new Intent(this,routerBean.getClazz());
                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            String call = data.getStringExtra("call");
            Log.e(TAG+">>>>>>>","MainActivity接收到的回调："+call);
        }
    }
}
