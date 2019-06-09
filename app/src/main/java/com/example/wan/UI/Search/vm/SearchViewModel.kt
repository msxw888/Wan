package com.example.wan.UI.Search.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wan.bean.BaseResponse
import com.example.wan.bean.HomeListResponse
import com.example.wan.repository.remote.NetworkDataimpl

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/5/17
 * @Version：
 *
 */
class SearchViewModel (private val repository : NetworkDataimpl) : ViewModel(){
    var searchdata = MutableLiveData<BaseResponse<HomeListResponse>>()

    fun getSearchList(page: Int = 0, k: String) {
        repository.fetchSearch(page,k,searchdata)
//        searchdata = repository.getSearchList()
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