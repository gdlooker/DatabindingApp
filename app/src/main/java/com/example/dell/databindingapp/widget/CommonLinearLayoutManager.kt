package com.example.dell.databindingapp.widget;

import android.content.Context
import androidx.annotation.Nullable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CommonLinearLayoutManager @JvmOverloads constructor(
        context: Context?, @Nullable orientation: Int = RecyclerView.VERTICAL,
        reverseLayout: Boolean = false
) : LinearLayoutManager(context, orientation, reverseLayout) {

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        try {
            super.onLayoutChildren(recycler, state)
        } catch (e: Exception) {

        }
    }
}