package com.example.wan.UI.Search.vm

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

    var total  = arrayOf(Int.MAX_VALUE)

    fun getSearchList(page: Int = 0, k: String) {
        if (page!=0&&page<total[0]){
            repository.fetchSearch(page, k, searchdata,total)
        }else if (page==0){
            repository.fetchSearch(page,k,searchdata,total)
        }
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