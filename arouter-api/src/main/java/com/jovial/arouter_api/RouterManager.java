package com.jovial.arouter_api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.util.LruCache;

import com.jovial.annotation.model.RouterBean;
import com.jovial.arouter_api.core.ARouterLoadGroup;
import com.jovial.arouter_api.core.ARouterLoadPath;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/24 0024]
 * About Class:[ 路由管理器]
 */
public class RouterManager {
    private static final String TAG = RouterManager.class.getName();
    //路由组名
    private String group;
    //路由Path路径
    private String path;
    private static RouterManager instance;
    //Lru缓存：key：类名  value：路由组Group加载接口
    private LruCache<String, ARouterLoadGroup> groupLruCache;
    //Lru缓存：key：类名 value：路由组path加载接口
    private LruCache<String, ARouterLoadPath> pathLruCache;
    //APT生成的类文件，后缀名（包名拼接）
    private static final String GROUP_FILE_PREFIX_NAME ="ARouter$$Group$$";

    public static RouterManager getInstance(){
        if(instance == null){
            synchronized(RouterManager.class){
                if(instance == null){
                    instance = new RouterManager();
                }
            }
        }
        return instance;
    }

    private RouterManager(){
        groupLruCache = new LruCache<>(163);
        pathLruCache = new LruCache<>(163);
    }

    /**
     *
     * @param path 传递具体路径
     * @return 需要的Router信息
     */
    public BundleManager build(String path){
        if (TextUtils.isEmpty(path) || !path.startsWith("/")){
            //不按照规范配置：如：/app/MainActivity
            throw new IllegalArgumentException("请按照规范配置：如：/app/MainActivity");
        }
        group = subFromPath2Group(path);
        //检查通过了path 和group
        this.path = path;
        return  new BundleManager();
    }

    private String subFromPath2Group(String path) {
        //开发者：path="/MainActivity"
        if(path.lastIndexOf("/") == 0){
            throw new IllegalArgumentException("请按照规范配置：如：/app/MainActivity");
        }

        //从第一个 / 到第二个 /中间截取
        String finalGroup = path.substring(1,path.indexOf("/",1));
        if(TextUtils.isEmpty(finalGroup)){
            //架构师定义规范，让开发者遵循
            throw new IllegalArgumentException("请按照规范配置：如：/app/MainActivity");
        }
        return finalGroup;
    }

    /**
     * 开始跳转
     * @param context 上下文
     * @param bundleManager 参数管理
     * @param code 这里的code可能是resultCode，也可以是requestCode,取决于isResult
     * @return 普通的跳转可以忽略，用于跨模块CALL接口
     */
    public Object navigation(Context context, BundleManager bundleManager, int code) {
        /** ARouter$$Group$$order -->ARouter$$Path$$order  -->Order_MainActivity  */
        //ARouter$$Group$$order
        String groupClassName = context.getPackageName() + ".apt." + GROUP_FILE_PREFIX_NAME + group;
        Log.e(TAG+">>>>>>",groupClassName);
        //读取路由组group类文件（缓存、懒加载）
        ARouterLoadGroup groupLoad = groupLruCache.get(group);
        try {
            if (groupLoad == null) {
                //加载APT路由组Group类文件ARouter$$Group$$personal

                Class<?> clazz = Class.forName(groupClassName);
                //初始化类文件
                groupLoad = (ARouterLoadGroup) clazz.newInstance();
                groupLruCache.put(group, groupLoad);
            }
            //判断
            if(groupLoad.loadGroup().isEmpty()){
                throw new RuntimeException("路由表组加载失败！");
            }
            //读取路由Path路径类文件缓存（懒加载）
            ARouterLoadPath pathLoad = pathLruCache.get(path);
            if (pathLoad == null) {
                //通过组Group加载接口，获取Path加载接口
                Class<? extends ARouterLoadPath> clazz = groupLoad.loadGroup().get(group);
                //初始化ARouter$$Path$$personal
                if(clazz != null)pathLoad = clazz.newInstance();
                if(pathLoad != null)pathLruCache.put(path, pathLoad);
            }
            //健壮判断
            if(pathLoad != null){
                if(pathLoad.loadPath().isEmpty()){
                    throw new RuntimeException("路由表路径加载失败！");
                }
                RouterBean routerBean = pathLoad.loadPath().get(path);
                if(routerBean != null){
                    //类型判断，方片拓展
                    switch(routerBean.getType()){
                        case ACTIVITY:
                            Intent intent = new Intent(context,routerBean.getClazz());
                            intent.putExtras(bundleManager.getBundle());

                            //注意：如果是startActivityForResult
                            if(bundleManager.isResult()){
                                ((Activity)context).setResult(code,intent);  //此时的code是resultCode
                                ((Activity)context).finish();
                            }

                            if(code > 0){
                                //跳转需要回调
                                ((Activity)context).startActivityForResult(intent,code,bundleManager.getBundle());//此时的code是requestCode
                            }else{
                                context.startActivity(intent,bundleManager.getBundle());
                            }
                            break;

                        case CALL:
                            return routerBean.getClazz().newInstance();

                        default:
                             break;
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
