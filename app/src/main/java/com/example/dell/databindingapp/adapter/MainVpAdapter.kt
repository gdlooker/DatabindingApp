package com.example.dell.databindingapp.adapter

import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.dell.databindingapp.mvvm.BaseFragment

class MainVpAdapter(fragmentManager:FragmentManager, fragmentlist: ArrayList<BaseFragment<out ViewDataBinding>>) :FragmentStatePagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    val fragmentlist = fragmentlist
    var titles: ArrayList<String>? = null
    override fun getCount(): Int {
        return fragmentlist.size
    }

    override fun getItem(position: Int): Fragment {
        return fragmentlist[position]
    }
}