package com.example.wan.UI.like

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wan.bean.BaseResponse
import com.example.wan.bean.HomeListResponse
import com.example.wan.repository.remote.NetworkDataimpl

class LikeViewModel(private val repository: NetworkDataimpl) : ViewModel() {
    var collectList = MutableLiveData<BaseResponse<HomeListResponse>>()

    fun getCollectList(page:Int = 0) {
        repository.getCollectResponse(page ,collectList)
    }


    // TODO: Implement the ViewModel
}
