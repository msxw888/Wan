package com.example.wan.UI.account

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wan.State.loginState
import com.example.wan.bean.RegisterResponse


/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/4/29
 * @Version：
 *
 */
class AccountViewModel(private val repository : AccountRepository) :ViewModel() {


    var mLoginData: MutableLiveData<loginState> = MutableLiveData()

    var mRegisterData: MutableLiveData<RegisterResponse> = MutableLiveData()

    fun getLoginData(username: String, password: String) {
        if (checkNotNull(username,password)){
            repository.login(username,password,mLoginData)

        }else {
            mLoginData.postValue(loginState(usern = "点此登录",state = false))
        }
    }

    fun getRegisterData(username: String,password: String,repassword:String){
        if (checkNotNull(username,password)){
            repository.register(username,password,repassword,mRegisterData)
        }else {
//            loadState.postValue(
//                State(
//                    StateType.TIPS,
//                    tip = R.string.accountOrPassword_empty
//                )
//            )
            mLoginData.postValue(loginState(usern = "点此登录",state = false))
        }
    }

    // 非空判断
    private fun checkNotNull(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}