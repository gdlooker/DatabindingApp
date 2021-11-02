package com.example.dell.databindingapp.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.example.dell.databindingapp.BaseActivity;
import com.example.dell.databindingapp.R;
import com.example.dell.databindingapp.databinding.ActivitySecondBinding;

import java.util.Objects;

/**
 * 第二个界面的Activity
 */
public class SecondActivity extends BaseActivity<ActivitySecondBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_second;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initData() {
        Objects.requireNonNull(getBindingView()).tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this, ThreeActivity.class));
            }
        });
    }
}
