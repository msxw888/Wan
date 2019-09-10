package com.example.wan.repository.remote

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wan.bean.*

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/3/31
 * @Version：
 *
 */
interface NetworkData {
//    val downloadhomelist :LiveData<BaseResponse>

    fun fetchhomelist(page: Int,netstate:MutableLiveData<Boolean>)

    fun gethomedata():LiveData<BaseResponse<HomeListResponse>>

    fun fetchbannerlist()

    fun getbannerdata():LiveData<BannerResponse>

    /**
     * 搜索
     */
    fun fetchSearch(page: Int, k: String,data: MutableLiveData<BaseResponse<HomeListResponse>>,total : Array<Int>)

    /**
     * 收藏列表
     */
    fun getCollectResponse(
        page: Int,
        data: MutableLiveData<BaseResponse<CollectRsp>>,
        netstate: MutableLiveData<Boolean>
    )

    fun getTree(data: MutableLiveData<BaseResponse<List<TopTreeRsp>>>)

    fun getArticle(page: Int, cid: Int, data: MutableLiveData<BaseResponse<TreeArticleRsp>>)
}