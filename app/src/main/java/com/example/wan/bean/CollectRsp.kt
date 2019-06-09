package com.example.wan.bean

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/6/9
 * @Version：
 *
 */
class CollectRsp(
    var curPage: Int,
    var datas: List<Article>,
    var total: Int
)