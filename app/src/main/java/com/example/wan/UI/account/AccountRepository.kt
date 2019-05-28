package com.example.wan.UI.account

import androidx.lifecycle.MutableLiveData
import com.example.wan.State.loginState
import com.example.wan.base.Preference
import com.example.wan.bean.BaseResponse
import com.example.wan.loge
import com.example.wan.bean.LoginResponse
import com.example.wan.bean.RegisterResponse
import com.example.wan.repository.remote.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/4/30
 * @Version：
 *
 */
class AccountRepository(private val network : RetrofitHelper) {
    /**
     * check login for SharedPreferences
     */
    var isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)
    /**
     * local username
     */
    var user: String? by Preference(Constant.USERNAME_KEY, "")
    /**
     * local password
     */
    var pwd: String? by Preference(Constant.PASSWORD_KEY, "")


//    fun login(username: String, password: String, mLoginData: MutableLiveData<loginState>) {
//        try {
//            network.getapi().loginWanAndroid(username, password).enqueue(object: Callback<LoginResponse> {
//                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
//                    loge("login>>>>>>>>>>","fail")
//                }
//
//                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
//                    if (response.)
//                }
//            })
//        } catch (e: Exception) {
//            loge("login:","exception")
//        }
//    }
    fun login(username: String, password: String, mLoginData: MutableLiveData<loginState>){
        network.getapi().loginWanAndroid(username, password).enqueue(object: Callback<BaseResponse<LoginResponse>?> {
            override fun onFailure(call: Call<BaseResponse<LoginResponse>?>, t: Throwable) {
                loge("login>>>>>>>>>>","请求失败")
            }

            override fun onResponse(
                call: Call<BaseResponse<LoginResponse>?>,
                response: Response<BaseResponse<LoginResponse>?>
            ) {
                response.body()?.let {
                    if (it.errorCode!=0){
                        mLoginData.postValue(loginState(usern = "点此登录",state = false))
                    }
                    else if(it.errorCode==0){
                        user = username
                        isLogin = true
                        mLoginData.postValue(loginState(usern = it.data.username,state = true))
                    }
                }
            }
        })
}

    fun register(
        username: String,
        password: String,
        repassword: String,
        mRegisterData: MutableLiveData<RegisterResponse>
    ) {
        try {
            network.getapi().registerWanAndroid(username, password,repassword).enqueue(object: Callback<RegisterResponse?> {
                override fun onFailure(call: Call<RegisterResponse?>, t: Throwable) {
                    loge("login>>>>>>>>>>","fail")
                }

                override fun onResponse(call: Call<RegisterResponse?>, response: Response<RegisterResponse?>) {
                    mRegisterData.postValue(response.body())
                }
            })
        } catch (e: Exception) {
            loge("Register:","exception")
        }
    }

}