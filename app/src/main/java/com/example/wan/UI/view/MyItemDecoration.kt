package com.example.wan.UI.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * 文件描述：
 * @author：WJH
 * @Creatdate：2019/9/7
 * @Version：
 *
 */
class MyItemDecoration : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        if(parent.getChildAdapterPosition(view)!=0){
            outRect.top = 10
        }
    }
}