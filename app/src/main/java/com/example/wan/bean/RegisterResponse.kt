package com.example.wan.bean

data class RegisterResponse(
        var username: String,
        var id: Int,
        var icon: String,
        var type: Int,
        var collectIds: List<Int>
)