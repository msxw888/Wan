package com.example.wan.UI.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wan.bean.BannerResponse
import com.example.wan.bean.BaseResponse
import com.example.wan.bean.HomeListResponse
import com.example.wan.repository.remote.NetworkDataimpl

class MainViewModel(private val repository : NetworkDataimpl) : ViewModel() {
    var _homedata = MutableLiveData<BaseResponse<HomeListResponse>>()
    var bannerdata = MutableLiveData<BannerResponse>()
//    val data: LiveData<BaseResponse>
//        get()= _homedata
    private val firstpage:Int = 0

    fun gethomelist(page:Int) {
        repository.fetchhomelist(page)
        _homedata = repository.gethomedata()
    }
    fun getfirstList(){
        repository.fetchhomelist(firstpage)
         _homedata = repository.gethomedata()
    }

    fun getbannerList(){
        repository.fetchbannerlist()
        bannerdata = repository.getbannerdata() as MutableLiveData<BannerResponse>
    }


}
