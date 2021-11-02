package com.example.dell.databindingapp.ui.activity

import android.content.Intent
import android.util.Log
import android.view.View
import com.example.dell.databindingapp.BaseActivity
import com.example.dell.databindingapp.R
import com.example.dell.databindingapp.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 子类的Activity
 */
class MainActivity() : BaseActivity<ActivityMainBinding>() {

    //子类Activity实现布局
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        Log.i("gdchent", "initData")
        bindingView?.tvStart?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                Log.i("gdchent", "initData_onclick")
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                startActivity(intent)
            }
        })
    }
}
