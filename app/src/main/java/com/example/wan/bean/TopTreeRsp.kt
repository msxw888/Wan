package com.example.wan.bean


data class TopTreeRsp(
        var children: List<SecondTreeRsp>,
        var name: String
){
        data class SecondTreeRsp(
                var id: Int,
                var name: String,
                var courseId: Int,
                var parentChapterId: Int,
                var order: Int,
                var visible: Int
        )
}