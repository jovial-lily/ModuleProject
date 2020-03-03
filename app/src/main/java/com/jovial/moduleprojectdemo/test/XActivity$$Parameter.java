package com.jovial.moduleprojectdemo.test;

import com.jovial.arouter_api.core.ParameterLoad;
import com.jovial.moduleprojectdemo.MainActivity;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/24 0024]
 * About Class:[ 参数类的模型，最终apt生成的模板]
 */
public class XActivity$$Parameter implements ParameterLoad {
    @Override
    public void loadParameter(Object target) {
        MainActivity t = (MainActivity)target;
//        t.name = t.getIntent().getStringExtra("name");
//        t.age = t.getIntent().getIntExtra("age",0);
    }
}
