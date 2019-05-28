package com.example.wan.bean

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/3/11
 * @Changedata：2019/3/11
 * @Version：
 *
 */
data class Article(
    var id: Int,
    var originId: Int,
    var title: String,
    var chapterId: Int,
    var chapterName: String?,
    var envelopePic: Any,
    var link: String,
    var author: String,
    var origin: Any,
    var publishTime: Long,
    var zan: Any,
    var desc: Any,
    var visible: Int,
    var niceDate: String,
    var courseId: Int,
    var collect: Boolean
)