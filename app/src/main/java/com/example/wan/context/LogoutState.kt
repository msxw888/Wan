package com.example.wan.context

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.wan.R
import com.example.wan.UI.account.LoginActivity
import com.example.wan.context.collect.CollectListener
import com.example.wan.toast

class LogoutState : UserState {
    override fun goTodoActivity(context: Context?) {
        goLoginActivity(context)
    }

    override fun collect(context: Context?, position: Int,listener: CollectListener) {
        goLoginActivity(context)
    }

    override fun login(context: Context?) {
        goLoginActivity(context)
    }

    override fun goCollectActivity(context: Context?) {
        goLoginActivity(context)
    }

    // 跳转到登录
    private fun goLoginActivity(context: Context?) {
        context?.let {
            it.toast(it.getString(R.string.login_please_login))
            Intent(context, LoginActivity::class.java).run {
                startActivity(context,this,null)
            }
        }
    }
}