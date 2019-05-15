package com.example.wan.bean

data class BaseResponse<T>(
    var errorCode: Int,
    var errorMsg: String?,
    var data: T
)