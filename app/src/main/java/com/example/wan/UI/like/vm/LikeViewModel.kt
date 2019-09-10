package com.example.wan.UI.like.vm

import androidx.lifecycle.MutableLiveData
import com.example.wan.base.BaseViewModel
import com.example.wan.bean.BaseResponse
import com.example.wan.bean.CollectRsp
import com.example.wan.repository.remote.NetworkDataimpl

class LikeViewModel(private val repository: NetworkDataimpl) : BaseViewModel() {
    var collectList = MutableLiveData<BaseResponse<CollectRsp>>()

    fun getCollectList(page:Int = 0) {
        repository.getCollectResponse(page ,collectList,netstate)
    }

}
