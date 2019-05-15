package com.example.wan.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.wan.repository.remote.RetrofitHelper
import com.example.wan.bean.Datas

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/3/30
 * @Version：
 *
 */
class Repository private constructor(private val network :RetrofitHelper){
    companion object {
        private var instance: Repository? = null

        fun newinstance(network: RetrofitHelper) : Repository = Repository(network)
    }
//    inner class UserRepository {
//        private val webservice: Webservice? = null
//        // ...
//        fun getUser(userId: Int): LiveData<User> {
//            // This isn't an optimal implementation. We'll fix it later.
//            val data = MutableLiveData<User>()
//            webservice!!.getUser(userId).enqueue(object : Callback<User> {
//                override fun onResponse(call: Call<User>, response: Response<User>) {
//                    data.setValue(response.body())
//                }
//
//                // Error case is left out for brevity.
//            })
//            return data
//        }
//    }

    private val _data = MutableLiveData<List<Datas>>()
    val data: LiveData<List<Datas>>
        get()= _data

//    fun requestandgetHomeList(page: Int): MutableLiveData<List<Datas>> {
//        network.getHomeList(page,object: Callback<BaseResponse> {
//            override fun onFailure(call: Call<BaseResponse>, t: Throwable) {
//                Log.e("请求失败","false")
//            }
//
//            override fun onResponse(call: Call<BaseResponse>, response: Response<BaseResponse>) {
//                val result = response.body()!!.data.datas
//                _homedata.postValue(result)
//            }
//        })
//        return _homedata
//
//    }
//
//    fun gethomeList(page: Int): LiveData<List<Datas>> {
//        requestandgetHomeList(page)
//        return data
//    }
//
//    fun getdefaulthomeList(): LiveData<List<Datas>> {
//        requestandgetHomeList(0)
//        return data
//    }
}