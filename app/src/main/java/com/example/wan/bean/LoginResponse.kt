package com.example.wan.bean

/**
 * author：
 * created：
 * desc：    登录返回类
 */
data class LoginResponse(
        var icon: String,
        var type: String,
        var collectIds: List<Int>,
        var username: String
)
