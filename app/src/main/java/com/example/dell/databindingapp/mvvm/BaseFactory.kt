package com.example.dell.databindingapp.mvvm
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


/**
 * viewModel构造工厂
 */
fun baseFactory(function: () -> ViewModel): ViewModelProvider.Factory {
    return object : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return function.invoke() as T
        }
    }

}