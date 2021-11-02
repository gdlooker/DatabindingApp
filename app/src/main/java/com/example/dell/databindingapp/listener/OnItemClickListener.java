package com.example.dell.databindingapp.listener;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.dell.databindingapp.base.adapter.BaseQuickAdapter;

/**
 * Author: WuShengjun
 * Date: 2021/2/25
 * Time: 16:40
 * Description: Interface definition for a callback to be invoked when an item in this
 * RecyclerView itemView has been clicked.
 */
public interface OnItemClickListener {
    /**
     * Callback method to be invoked when an item in this RecyclerView has
     * been clicked.
     *
     * @param adapter  the adapter
     * @param view     The itemView within the RecyclerView that was clicked (this
     *                 will be a view provided by the adapter)
     * @param position The position of the view in the adapter.
     */
    void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position);
}
