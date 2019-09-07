package com.example.wan.UI.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.RecyclerView

/**
 * 文件描述：自定义轮播图Recycleview
 * @author：WJH
 * @Creatdata：2019/4/16
 * @Version：
 *
 */
class HorizontalRecyclerView : RecyclerView {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context, attrs, defStyle
    )

    /**
     * don't intercept touch
     * set parent不拦截触摸事件
     */
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }
}