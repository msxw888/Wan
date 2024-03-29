package com.example.wan.UI.Search.adapter

import android.content.Context
import android.text.Html
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.wan.R
import com.example.wan.bean.Article

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/5/17
 * @Version：
 *
 */
class SearchAdapter(val context: Context, data: MutableList<Article>) : BaseQuickAdapter<Article, BaseViewHolder>(R.layout.article_list_item, data) {
    override fun convert(helper: BaseViewHolder, item: Article?) {
        item ?: return
        @Suppress("DEPRECATION")
        helper.setText(R.id.homeItemTitle, Html.fromHtml(item.title))
            .setText(R.id.homeItemAuthor, item.author)
            .setText(R.id.homeItemType, item.chapterName)
            .setText(R.id.homeItemDate,
                if (item.originId != 0)
                    context.getString(R.string.bookmark_time, item.niceDate)
                else item.niceDate
            )
            .addOnClickListener(R.id.homeItemType)
//            .setTextColor(R.id.homeItemType, context.resources.getColor(R.color.colorPrimary))
            .linkify(R.id.homeItemType)
            .setImageResource(R.id.homeItemLike, isCollect(item))
            .addOnClickListener(R.id.homeItemLike)
    }

    private fun isCollect(article: Article): Int = if (article.collect) R.drawable.ic_favorite else R.drawable.ic_action_no_like
}