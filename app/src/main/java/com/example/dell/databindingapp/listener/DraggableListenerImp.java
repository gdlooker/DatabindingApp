package com.example.dell.databindingapp.listener;

import androidx.annotation.Nullable;

/**
 * Author: WuShengjun
 * Date: 2021/2/25
 * Time: 16:40
 * Description:
 */
public interface DraggableListenerImp {

    void setOnItemDragListener(@Nullable OnItemDragListener onItemDragListener);

    void setOnItemSwipeListener(@Nullable OnItemSwipeListener onItemSwipeListener);
}
