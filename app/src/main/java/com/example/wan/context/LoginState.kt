package com.example.wan.context

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.example.wan.UI.account.LoginActivity
import com.example.wan.context.collect.CollectListener


class LoginState : UserState {

    // 登录状态 直接跳转到todo 界面
    override fun goTodoActivity(context: Context?) {
//        context?.startActivity<TodoActivity>()
    }

    override fun collect(context: Context?, position: Int, listener: CollectListener) {
        // 发起收藏
        listener.collect(position)
    }

    // 已登录状态 无须登录 不做任何操作
    override fun login(context: Context?) {}

    override fun goCollectActivity(context: Context?) {
        context?.let {
            Intent(context, LoginActivity::class.java).run {
            ContextCompat.startActivity(context, this, null)
            }
        }

    }
}