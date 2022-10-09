package com.example.dell.databindingapp.ui.activity

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.viewpager.widget.ViewPager
import com.example.dell.databindingapp.BaseActivity
import com.example.dell.databindingapp.R
import com.example.dell.databindingapp.adapter.MainVpAdapter
import com.example.dell.databindingapp.databinding.ActivityMainBinding
import com.example.dell.databindingapp.ui.fragment.ContactFragment
import com.example.dell.databindingapp.ui.fragment.HomeFragment

/**
 * 子类的Activity
 */
class MainActivity() : BaseActivity<ActivityMainBinding>(),View.OnClickListener {

    val TAG:String ="MainActivity";

    val vpOnclicListener by lazy {
        object : ViewPager.SimpleOnPageChangeListener() {
            override fun onPageSelected(position: Int) {
                Log.i(TAG, "position${position}")
                Toast.makeText(this@MainActivity,position.toString(),Toast.LENGTH_SHORT)
            }
        }
    }
    //子类Activity实现布局
    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        bindingView?.llChat?.setOnClickListener(this)
        bindingView?.llContact?.setOnClickListener(this)
        val fragmentlist = arrayListOf(HomeFragment(),ContactFragment());
        val fragmentManager=supportFragmentManager
        bindingView?.viewPager?.adapter = MainVpAdapter(fragmentManager, fragmentlist = fragmentlist);
        bindingView?.viewPager?.addOnPageChangeListener(vpOnclicListener)
    }

    override fun onClick(view: View?) {
        when(view?.id){
            R.id.ll_chat -> {
                bindingView?.viewPager?.setCurrentItem(0)
                baseTitle?.setText(R.string.chat_title)
            }
            R.id.ll_contact-> {
                bindingView?.viewPager?.setCurrentItem(1)
                baseTitle?.setText(R.string.contact_title)
            }
        }
    }
}
