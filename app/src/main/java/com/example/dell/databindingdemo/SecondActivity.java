package com.example.dell.databindingdemo;

import android.content.Intent;
import android.databinding.ViewDataBinding;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dell.databindingdemo.databinding.ActivitySecondBinding;

/**
 * 第二个界面的Activity
 */
public class SecondActivity extends BaseActivity<ActivitySecondBinding>{

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second;
    }

    @Override
    protected void initData() {
        getBindingView().tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,ThreeActivity.class));
            }
        });
    }
}
