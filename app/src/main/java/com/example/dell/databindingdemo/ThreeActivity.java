package com.example.dell.databindingdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dell.databindingdemo.databinding.ActivityThreeBinding;

public class ThreeActivity extends BaseActivity<ActivityThreeBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_three;
    }

    @Override
    protected void initData() {

        getBindingView().tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ThreeActivity.this,"老哥点击了",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
