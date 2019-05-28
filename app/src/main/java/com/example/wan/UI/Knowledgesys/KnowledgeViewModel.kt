package com.example.wan.UI.Knowledgesys

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wan.bean.BaseResponse
import com.example.wan.bean.TopTreeRsp
import com.example.wan.bean.TreeArticleRsp
import com.example.wan.loge
import com.example.wan.repository.remote.NetworkDataimpl

class KnowledgeViewModel(private val repository: NetworkDataimpl) : ViewModel() {

    var mTreeData: MutableLiveData<BaseResponse<List<TopTreeRsp>>> = MutableLiveData()
    var mTreeArticleData: MutableLiveData<BaseResponse<TreeArticleRsp>> = MutableLiveData()

    fun getTree() {
        repository.getTree(mTreeData)
    }

    fun getArticle(cid: Int, page: Int) {
        repository.getArticle(page, cid, mTreeArticleData)
    }
}
