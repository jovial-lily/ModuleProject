package com.jovial.moduleprojectdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jovial.annotation.ARouter;

@ARouter(path = "/app/OrderActivity")
public class OrderActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
    }
}
