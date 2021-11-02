package com.example.dell.databindingapp.listener;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.dell.databindingapp.base.adapter.BaseQuickAdapter;


/**
 * Author: WuShengjun
 * Date: 2021/2/25
 * Time: 16:40
 * Description:
 */
public interface OnItemChildLongClickListener {
    /**
     * callback method to be invoked when an item in this view has been
     * click and held
     *
     * @param adapter  this BaseQuickAdapter adapter
     * @param view     The childView whihin the itemView that was clicked and held.
     * @param position The position of the view int the adapter
     * @return true if the callback consumed the long click ,false otherwise
     */
    boolean onItemChildLongClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position);
}
