package com.jovial.library.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/19 0019]
 * About Class:[ 全局路径记录器（根据子模块分组）  这种方式最终被路由的方式代替 ]
 */
public class RecordPathManager {
    /*
    key:组（例如：order，personal。。）   value：当前组下所有activity路径信息
     */
    private static Map<String, List<PathBean>> groupMap = new HashMap<>();


    /**
     *将路径信息加入全局Map
     * @param groupName 组名
     * @param pathName 路径名
     * @param clazz 类
     */
    public static void joinGroup(String groupName,String pathName,Class<?> clazz){
        List<PathBean> list = groupMap.get(groupName);
        if(list == null){
            list = new ArrayList<>();
            list.add(new PathBean(pathName,clazz));
            groupMap.put(groupName,list);
        }else{
            for(PathBean p:list){
                if(!pathName.equals(p.getPath())){
                    list.add(p);
                    groupMap.put(groupName,list);
                }
            }
        }
    }

    /**
     * 根据组名和路径名获取 类对象 达到跳转的目的
     * @param groupName 组名
     * @param pathName 路径名
     * @return 要跳转的类对象
     */
    public static Class<?> getTargetClass(String groupName,String pathName){
        List<PathBean> list = groupMap.get(groupName);
        if(list != null){
            for(PathBean p:list){
                if(pathName.equalsIgnoreCase(p.getPath())){
                    return p.getClazz();
                }
            }
        }
        return null;
    }
}
