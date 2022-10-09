package com.example.dell.databindingapp.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dell.databindingapp.bean.menu.MenuItem
import java.io.Serializable

class BaseViewModel: Serializable,ViewModel() {

    //MenuItem点击监听
    var onMenuItemClick = MutableLiveData<MenuItem>()


}