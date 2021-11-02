package com.example.dell.databindingapp.ui.activity;

import android.os.Build;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.dell.databindingapp.BaseActivity;
import com.example.dell.databindingapp.R;
import com.example.dell.databindingapp.databinding.ActivityThreeBinding;

import java.util.Objects;

public class ThreeActivity extends BaseActivity<ActivityThreeBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_three;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void initData() {

        Objects.requireNonNull(getBindingView()).tvStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ThreeActivity.this,"老哥点击了",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
