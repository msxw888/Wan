package com.example.wan.UI.like.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wan.bean.BaseResponse
import com.example.wan.bean.CollectRsp
import com.example.wan.bean.HomeListResponse
import com.example.wan.repository.remote.NetworkDataimpl

class LikeViewModel(private val repository: NetworkDataimpl) : ViewModel() {
    var collectList = MutableLiveData<BaseResponse<CollectRsp>>()

    fun getCollectList(page:Int = 0) {
        repository.getCollectResponse(page ,collectList)
    }

}
