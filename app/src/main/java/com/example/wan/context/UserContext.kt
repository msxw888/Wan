package com.example.wan.context

import Constant
import android.app.Activity
import android.content.Context
import com.example.wan.base.Preference
import com.example.wan.context.collect.CollectListener

class UserContext private constructor() {
    /**
     * check login for SharedPreferences
     */
    var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)


    // 设置默认状态
    var mState: UserState = if (isLogin) LoginState() else LogoutState()

    companion object {
        val instance = Holder.INSTANCE
    }

    // 内部类 单利
    private object Holder {
        val INSTANCE = UserContext()
    }

    // 收藏
    fun collect(context: Context?, position: Int, listener: CollectListener) {
        mState.collect(context, position, listener)
    }

    // 跳转到todo
    fun goTodoActivity(context: Context?) {
        mState.goTodoActivity(context)
    }

    // 跳转去登录
    fun login(context: Activity?) {
        mState.login(context)
    }

    fun loginSuccess(username: String) {
        // 改变 sharedPreferences   isLogin值
        isLogin = true
        instance.mState = LoginState()

        // 登录成功 回调 -> DrawerLayout -> 个人信息更新状态
//        LoginSucState.notifyLoginState(username, collectIds)
    }

    fun logoutSuccess() {
        instance.mState = LogoutState()

        // 清除 cookie、登录缓存
        Preference.clear()

//        LoginSucState.notifyLoginState("未登录", null)
    }
}