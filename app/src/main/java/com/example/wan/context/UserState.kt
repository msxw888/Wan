package com.example.wan.context

import android.content.Context
import com.example.wan.context.collect.CollectListener


interface UserState {

    fun collect(context: Context?, position: Int, listener: CollectListener)

    fun login(context: Context?)

    fun goTodoActivity(context: Context?)

    fun goCollectActivity(context: Context?)
}