package com.example.wan.repository.remote

import androidx.lifecycle.LiveData
import com.example.wan.bean.BannerResponse
import com.example.wan.bean.BaseResponse
import com.example.wan.bean.HomeListResponse

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/3/31
 * @Version：
 *
 */
interface NetworkData {
//    val downloadhomelist :LiveData<BaseResponse>

    fun fetchhomelist(page: Int)

    fun gethomedata():LiveData<BaseResponse<HomeListResponse>>

    fun fetchbannerlist()

    fun getbannerdata():LiveData<BannerResponse>
}