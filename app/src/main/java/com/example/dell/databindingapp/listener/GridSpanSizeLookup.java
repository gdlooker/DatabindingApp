package com.example.dell.databindingapp.listener;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;

/**
 * Author: WuShengjun
 * Date: 2021/2/25
 * Time: 16:40
 * Description:
 */
public interface GridSpanSizeLookup {

    int getSpanSize(@NonNull GridLayoutManager gridLayoutManager, int viewType, int position);
}
