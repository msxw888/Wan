package com.example.wan.bean

data class HomeListResponse(
    var curPage: Int,
    var datas: List<Datas>,
    var offset: Int,
    var over: Boolean,
    var pageCount: Int,
    var size: Int,
    var total: Int
)