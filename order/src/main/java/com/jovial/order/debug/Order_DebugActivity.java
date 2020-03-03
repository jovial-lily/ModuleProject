package com.jovial.order.debug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jovial.order.R;
/**
 * Created by:[ Jovial ]
 * Created date:[ 2020/1/17 0017]
 * About Class:[ 这些代码在gradle配置后，集成化打包不会打包到apk]
 */
public class Order_DebugActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_activity_debug);
    }
}
