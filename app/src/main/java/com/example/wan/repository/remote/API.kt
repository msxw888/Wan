package com.example.wan.repository.remote

import com.example.wan.bean.*
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.http.*

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/3/24
 * @Changedata：2019/3/24
 * @Version：
 *
 */
interface API {

//    companion object {
//        operator fun invoke(): API {
//            return RetrofitHelper.getapi()
//        }
//    }
    /**
     * 首页数据
     * https://www.wanandroid.com/article/list/0/json
     * @param page page
     * baseurl:https://www.wanandroid.com
     */

    @GET("/article/list/{page}/json")
    fun getHomeList(
        @Path("page") page: Int
    ): Call<BaseResponse<HomeListResponse>>

    @GET("/banner/json")
    fun getBannerList() :Call<BannerResponse>


    /**
     * 登录
     * @param username username
     * @param password password
     * @return Deferred<LoginResponse>
     */
    @POST("/user/login")
    @FormUrlEncoded
    fun loginWanAndroid(
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<BaseResponse<LoginResponse>>

    /**
     * 注册
     * @param username username
     * @param password password
     * @param repassword repassword
     * @return Call<LoginResponse>
     */
    @POST("/user/register")
    @FormUrlEncoded
    fun registerWanAndroid(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassowrd: String
    ): Call<RegisterResponse>

    /**
     * 获取自己收藏的文章列表
     * @param page page
     * @return Call<BaseResponse<HomeListResponse>>
     */
    @GET("/lg/collect/list/{page}/json")
    fun getLikeList(
        @Path("page") page: Int
    ): Call<BaseResponse<HomeListResponse>>

    /**
     * 收藏文章
     * @param id id
     * @return Call<BaseResponse<HomeListResponse>>
     */
    @POST("/lg/collect/{id}/json")
    fun addCollectArticle(
        @Path("id") id: Int
    ): Call<BaseResponse<HomeListResponse>>

    /**
     * 搜索
     * @param page page
     * @param k POST search key
     * @return Call<BaseResponse<HomeListResponse>>
     */
    @POST("/article/query/{page}/json")
    @FormUrlEncoded
    fun getSearchList(
        @Path("page") page: Int,
        @Field("k") k: String
    ): Call<BaseResponse<HomeListResponse>>

    /**
     * 知识体系
     * https://www.wanandroid.com/tree/json
     */
    @GET("/tree/json")
    fun getTypeTreeList(): Call<BaseResponse<List<TopTreeRsp>>>

    /**
     * 知识体系下的文章
     * https://www.wanandroid.com/article/list/0/json?cid=168
     * @param page page
     * @param cid cid
     */
    @GET("/article/list/{page}/json")
    fun getTreeArticleList(
        @Path("page") page: Int,
        @Query("cid") cid: Int
    ): Call<BaseResponse<TreeArticleRsp>>
}