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
//    private val queryText = MutableLiveData<Int>()
//    private val repoResult = Transformations.map(queryText) {
//        repository.getHomeList(it)
//    }
//    val posts = Transformations.switchMap(repoResult, { it.pagedList })!!
//    val networkState = Transformations.switchMap(repoResult, { it.networkState })!!
//    val refreshState = Transformations.switchMap(repoResult, { it.refreshState })!!
//
//    fun refresh() {
//        repoResult.value?.refresh?.invoke()
//    }
//
//    fun retry() {
//        val listing = repoResult?.value
//        listing?.retry?.invoke()
//    }

}
