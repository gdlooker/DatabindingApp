package com.example.dell.databindingdemo

import android.content.Intent
import android.databinding.ViewDataBinding
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.dell.databindingdemo.databinding.ActivityMainBinding
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
        Log.i("gdchent","initData")
       bindingView?.tvStart?.setOnClickListener(object :View.OnClickListener{
           override fun onClick(v: View?) {
               Log.i("gdchent","initData_onclick")
                startActivity(Intent(this@MainActivity,SecondActivity::class.java))
           }
       })
    }
}
