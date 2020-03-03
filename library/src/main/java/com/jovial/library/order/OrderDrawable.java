package com.jovial.library.order;

import com.jovial.arouter_api.core.Call;

/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/2/25 0025]
 * About Class:[ 在公共库中写一个共有的接口提供给外部模块访问 ]
 */
public interface OrderDrawable extends Call {

    int getDrawable();
}
