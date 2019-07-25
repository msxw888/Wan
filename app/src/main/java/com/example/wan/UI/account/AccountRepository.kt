package com.example.wan.UI.account

import Constant
import androidx.lifecycle.MutableLiveData
import com.example.wan.State.LoginState
import com.example.wan.base.Preference
import com.example.wan.bean.*
import com.example.wan.loge
import com.example.wan.repository.remote.RetrofitHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 文件描述：
 * @author：WJH
 * @Creatdate：2019/4/30
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


//    fun login(username: String, password: String, mLoginData: MutableLiveData<LoginState>) {
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
    fun login(username: String, password: String, mLoginData: MutableLiveData<LoginState>){
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
                        mLoginData.postValue(LoginState(usern = "点此登录",state = false))
                    }
                    else if(it.errorCode==0){
                        user = username
                        isLogin = true
                        mLoginData.postValue(LoginState(usern = it.data.username,state = true))
                    }
                }
            }
        })
}

    fun register(
        username: String,
        password: String,
        repassword: String,
        mRegisterData: MutableLiveData<BaseResponse<RegisterResponse>>
    ) {
        try {
            network.getapi().registerWanAndroid(username, password,repassword).enqueue(object: Callback<BaseResponse<RegisterResponse>?> {
                override fun onFailure(call: Call<BaseResponse<RegisterResponse>?>, t: Throwable) {
                    loge("login>>>>>>>>>>","fail")
                }

                override fun onResponse(
                    call: Call<BaseResponse<RegisterResponse>?>,
                    response: Response<BaseResponse<RegisterResponse>?>
                ) {
                    mRegisterData.postValue(response.body())
                }
            })
        } catch (e: Exception) {
            loge("Register:","exception")
        }
    }


    fun collect(id:Int,mCollectData: MutableLiveData<BaseResponse<HomeListResponse>>){
        try {
            network.getapi().addCollectArticle(id).enqueue(object: Callback<BaseResponse<HomeListResponse>?> {
                override fun onFailure(call: Call<BaseResponse<HomeListResponse>?>, t: Throwable) {
                    loge("请求>>>>>>>>>>","fail")
                }

                override fun onResponse(
                    call: Call<BaseResponse<HomeListResponse>?>,
                    response: Response<BaseResponse<HomeListResponse>?>
                ) {
                    mCollectData.postValue(response.body())
                }
            })
        } catch (e: Exception) {
        }
    }

    fun removeCollect(
        id: Int,
        originId: Int,
        mRequestCollectData: MutableLiveData<BaseResponse<EmptyRsp>>
    ) {
        try {
            network.getapi().removeCollectArticle(id,originId).enqueue(object: Callback<BaseResponse<EmptyRsp>?> {
                override fun onFailure(call: Call<BaseResponse<EmptyRsp>?>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<BaseResponse<EmptyRsp>?>,
                    response: Response<BaseResponse<EmptyRsp>?>
                ) {
                    mRequestCollectData.postValue(response.body())
                }
            })
        } catch (e: Exception) {
        }
    }

    fun unCollect(id: Int,mRequestCollectData: MutableLiveData<BaseResponse<EmptyRsp>>){
        network.getapi().unCollect(id).enqueue(object: Callback<BaseResponse<EmptyRsp>?> {
            override fun onFailure(call: Call<BaseResponse<EmptyRsp>?>, t: Throwable) {

            }

            override fun onResponse(call: Call<BaseResponse<EmptyRsp>?>, response: Response<BaseResponse<EmptyRsp>?>) {
                mRequestCollectData.postValue(response.body())
            }
        })
    }
}