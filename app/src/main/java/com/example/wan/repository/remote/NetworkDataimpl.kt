package com.example.wan.repository.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wan.bean.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/3/31
 * @Version：
 *
 */
class NetworkDataimpl(private val network :RetrofitHelper) : NetworkData {

    private  val _downloadhomelist = MutableLiveData<BaseResponse<HomeListResponse>>()
    private  val bannerlist = MutableLiveData<BannerResponse>()

    /**
     * 网络获取homelist
     */
    override fun fetchhomelist(page: Int) {
        try {
            network.getapi().getHomeList(page).enqueue(object: Callback<BaseResponse<HomeListResponse>?> {
                override fun onFailure(call: Call<BaseResponse<HomeListResponse>?>, t: Throwable) {
                    Log.e(">>>>>>>>>","请求失败")
                }

                override fun onResponse(call: Call<BaseResponse<HomeListResponse>?>, response: Response<BaseResponse<HomeListResponse>?>) {
                    if (response.body()!!.errorCode==0){
                        _downloadhomelist.postValue(response.body())
                    }
                }
            })
        }
        catch (e:Exception){

        }
    }

    override fun gethomedata(): MutableLiveData<BaseResponse<HomeListResponse>> {
        return _downloadhomelist
    }

    /**
     * 网络获取banner
     */
    override fun fetchbannerlist() {
        try {
            network.getapi().getBannerList().enqueue(object: Callback<BannerResponse?> {
                override fun onFailure(call: Call<BannerResponse?>, t: Throwable) {
                    Log.e("Banner>>>>>>>>>","请求失败")
                }

                override fun onResponse(call: Call<BannerResponse?>, response: Response<BannerResponse?>) {
                    bannerlist.postValue(response.body())
                }
            })
        } catch (e: Exception) {
            Log.e("fetchbannerlist:","exception")
        }
    }

    /**
     * 从仓库获取banner
     */
    override fun getbannerdata(): LiveData<BannerResponse> {
        return bannerlist
    }



}