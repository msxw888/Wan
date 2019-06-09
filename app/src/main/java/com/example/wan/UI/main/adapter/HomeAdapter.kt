package com.example.wan.UI.main.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.wan.R
import com.example.wan.bean.Article


class HomeAdapter( data: List<Article>) :BaseQuickAdapter<Article,BaseViewHolder>(R.layout.article_list_item, data) {
    override fun convert(helper: BaseViewHolder, item: Article?) {
        item ?: return
        @Suppress("DEPRECATION")
        helper.setText(R.id.homeItemTitle, item.title)
            .setText(R.id.homeItemAuthor, item.author)
            .setText(R.id.homeItemType, item.chapterName)
            .addOnClickListener(R.id.homeItemType)
//            .setTextColor(R.id.homeItemType, context.resources.getColor(R.color.colorPrimary))
            .linkify(R.id.homeItemType)
            .setText(R.id.homeItemDate, item.niceDate)
            .setImageResource(
                R.id.homeItemLike,
                if (item.originId != 0 || item.collect) R.drawable.ic_favorite else R.drawable.aixin
            )
            .addOnClickListener(R.id.homeItemLike)
    }

}
