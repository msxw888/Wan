package com.example.wan.bean

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/3/11
 * @Changedata：2019/3/11
 * @Version：
 *
 */

data class Data(
    var offset: Int,
    var size: Int,
    var total: Int,
    var pageCount: Int,
    var curPage: Int,
    var over: Boolean,
    var datas: List<Datas>?
)