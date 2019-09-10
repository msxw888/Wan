package com.example.wan.UI.main.vm

import androidx.lifecycle.MutableLiveData
import com.example.wan.base.BaseViewModel
import com.example.wan.bean.BannerResponse
import com.example.wan.bean.BaseResponse
import com.example.wan.bean.HomeListResponse
import com.example.wan.repository.remote.NetworkDataimpl

class MainViewModel(private val repository : NetworkDataimpl) : BaseViewModel() {
    var _homedata = MutableLiveData<BaseResponse<HomeListResponse>>()
    var bannerdata = MutableLiveData<BannerResponse>()
//    val data: LiveData<BaseResponse>
//        get()= _homedata
    private val firstpage:Int = 0

    fun gethomelist(page:Int) {
        repository.fetchhomelist(page,netstate)
        _homedata = repository.gethomedata()
    }
    fun getfirstList(){
        repository.fetchhomelist(firstpage,netstate)
         _homedata = repository.gethomedata()
    }

    fun getbannerList(){
        repository.fetchbannerlist()
        bannerdata = repository.getbannerdata() as MutableLiveData<BannerResponse>
    }

}
