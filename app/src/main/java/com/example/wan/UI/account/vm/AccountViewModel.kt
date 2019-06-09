package com.example.wan.UI.account.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wan.State.loginState
import com.example.wan.UI.account.AccountRepository
import com.example.wan.bean.BaseResponse
import com.example.wan.bean.EmptyRsp
import com.example.wan.bean.HomeListResponse
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

    var mRegisterData: MutableLiveData<BaseResponse<RegisterResponse>> = MutableLiveData()

    var mLikeData:MutableLiveData<BaseResponse<HomeListResponse>> = MutableLiveData()

    var mRequestCollectData: MutableLiveData<BaseResponse<EmptyRsp>> = MutableLiveData()

    fun getLoginData(username: String, password: String) {
        if (checkNotNull(username,password)){
            repository.login(username,password,mLoginData)

        }else {
            mLoginData.postValue(loginState(usern = "点此登录",state = false))
        }
    }

    fun getRegister(username: String,password: String,repassword:String){
        if (checkNotNull(username,password)){
            repository.register(username,password,repassword,mRegisterData)
        }else {
            mLoginData.postValue(loginState(usern = "点此登录",state = false))
        }
    }


    fun Collect(id:Int){
        repository.collect(id, mLikeData)
    }

    fun removeCollectArticle(id: Int, originId:Int) {
        repository.removeCollect(id,originId,mRequestCollectData)
    }

    fun unCollect(id: Int){
        repository.unCollect(id,mRequestCollectData)
    }

    // 非空判断
    private fun checkNotNull(username: String, password: String): Boolean {
        return username.isNotEmpty() && password.isNotEmpty()
    }
}