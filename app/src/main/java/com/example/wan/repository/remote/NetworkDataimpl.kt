package com.example.wan.repository.remote

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wan.bean.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/3/31
 * @Version：
 *
 */
class NetworkDataimpl(private val network : RetrofitHelper) : NetworkData {

    private  val _downloadhomelist = MutableLiveData<BaseResponse<HomeListResponse>>()


    /**
     * 网络获取homelist
     */
    override fun fetchhomelist(page: Int,netstate:MutableLiveData<Boolean>) {
        try {
            network.getapi().getHomeList(page).enqueue(object: Callback<BaseResponse<HomeListResponse>?> {
                override fun onFailure(call: Call<BaseResponse<HomeListResponse>?>, t: Throwable) {
                    Log.e(">>>>>>>>>","请求失败")
                    netstate.postValue(false)
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


    private  val bannerlist = MutableLiveData<BannerResponse>()
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

//    private val _downloadedSearch = MutableLiveData<BaseResponse<HomeListResponse>>()
//    val downloadedSearch: LiveData<BaseResponse<HomeListResponse>>
//        get() = _downloadedSearch

    /**
     *
     */
//    fun getSearchList() :MutableLiveData<BaseResponse<HomeListResponse>> = _downloadedSearch


    override fun fetchSearch(page: Int, k: String,data: MutableLiveData<BaseResponse<HomeListResponse>>,total : Array<Int> ) {
        try {
            network.getapi().getSearchList(page, k).enqueue(object: Callback<BaseResponse<HomeListResponse>?> {
                override fun onFailure(call: Call<BaseResponse<HomeListResponse>?>, t: Throwable) {

                }
                override fun onResponse(
                    call: Call<BaseResponse<HomeListResponse>?>,
                    response: Response<BaseResponse<HomeListResponse>?>
                ) {
//                    _downloadedSearch.postValue(response.body())
                    data.postValue(response.body())
                    Log.e("downfetchsearchlist:",response.body()?.data?.pageCount.toString())
                    total[0] = response.body()?.data?.total!!
                }
            })

        } catch (e:Exception) {

        }
    }

    override fun getCollectResponse(
        page: Int,
        data: MutableLiveData<BaseResponse<CollectRsp>>,
        netstate: MutableLiveData<Boolean>
    ) {
        try {
            network.getapi().getLikeList(page).enqueue(object: Callback<BaseResponse<CollectRsp>?> {
                override fun onFailure(call: Call<BaseResponse<CollectRsp>?>, t: Throwable) {
                    Log.e("like>>>>>>>>>","请求失败")
                    netstate.postValue(false)
                }

                override fun onResponse(
                    call: Call<BaseResponse<CollectRsp>?>,
                    response: Response<BaseResponse<CollectRsp>?>
                ) {
                    if (response.body()?.errorCode !=0){
                        Log.e("like>>>>>>>>>",response.body()?.errorMsg!!)
                        data.postValue(response.body())
                    }
                    else if (response.body()?.errorCode==0){
                        data.postValue(response.body())
                    }
                }
            })
        } catch (e: Exception) {

        }

    }

    override fun getTree(data: MutableLiveData<BaseResponse<List<TopTreeRsp>>>) {
        try {
            network.getapi().getTypeTreeList().enqueue(object: Callback<BaseResponse<List<TopTreeRsp>>?> {
                override fun onFailure(call: Call<BaseResponse<List<TopTreeRsp>>?>, t: Throwable) {
                    Log.e("type>>>>>>>>>","请求失败")

                }
                override fun onResponse(
                    call: Call<BaseResponse<List<TopTreeRsp>>?>,
                    response: Response<BaseResponse<List<TopTreeRsp>>?>
                ) {
                    if (response.body()?.errorCode !=0){
                        Log.e("typeERROR>>>>>>>>>",response.body()?.errorMsg!!)
                        data.postValue(response.body())
                    }
                    else if (response.body()?.errorCode==0){
                        data.postValue(response.body())
                    }
                }
            })
        } catch (e: Exception) {

        }
    }

    override fun getArticle(page: Int, cid: Int, data: MutableLiveData<BaseResponse<TreeArticleRsp>>) {
        network.getapi().getTreeArticleList(page, cid).enqueue(object: Callback<BaseResponse<TreeArticleRsp>?> {
            override fun onFailure(call: Call<BaseResponse<TreeArticleRsp>?>, t: Throwable) {
                Log.e("typearticle>>>>>>>>>","请求失败")
            }

            override fun onResponse(
                call: Call<BaseResponse<TreeArticleRsp>?>,
                response: Response<BaseResponse<TreeArticleRsp>?>
            ) {
                if (response.body()?.errorCode !=0){
                    Log.e("type>>>>>>>>>",response.body()?.errorMsg!!)
                    data.postValue(response.body())
                }
                else if (response.body()?.errorCode==0){
                    data.postValue(response.body())
                }
            }
        })
    }


    /**
     * 拓展方法，将异步回调改为同步的形式
     * @receiver Call<T>
     * @return T
     */
    suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(RuntimeException("response body is null"))
                }
            })
        }
    }
}