package com.jovial.arouter_api;

import android.app.Activity;
import android.util.LruCache;

import com.jovial.arouter_api.core.ParameterLoad;

import javax.annotation.Nonnull;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/24 0024]
 * About Class:[ 参数Parameter加载管理器]
 */
public class ParameterManager {
    //单例模式
    private static ParameterManager instance;
    //使用lru缓存，key：类名   value：参数Parameter加载接口
    //为什么使用缓存，因为如果页面一跳转到页面二传了参数过去，返回后立刻又跳转过去
    //这样就不需要再加载参数，直接从缓存上面取，节约性能
    private LruCache<String, ParameterLoad> cache;
    //APT生成的获取参数类文件，后缀名
    private static final String FILE_SUFFIX_NAME="$$Parameter";

    /**
     * 单例方法：全局唯一
     */

    public static ParameterManager getInstance(){
        if(instance == null){
            synchronized (ParameterManager.class){
                if(instance == null){
                    instance = new ParameterManager();
                }
            }
        }
        return instance;
    }

    /**
     * 私有化构造函数
     */
    private ParameterManager() {
        //初始化并赋值缓存中条目的最大值
        cache = new LruCache<>(163);

    }

    public void loadParameter(@Nonnull Activity activity) {
        String className = activity.getClass().getName();
        ParameterLoad iParameter = cache.get(className);
        //缓存中没有
        try {
            if (iParameter == null) {
                Class<?> clazz = Class.forName(className + FILE_SUFFIX_NAME);
                iParameter = (ParameterLoad)clazz.newInstance();
                cache.put(className,iParameter);
            }
            iParameter.loadParameter(activity);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
