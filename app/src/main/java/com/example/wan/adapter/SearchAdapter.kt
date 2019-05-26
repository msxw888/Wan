package com.example.wan.adapter

import android.text.Html
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.wan.R
import com.example.wan.bean.Datas

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/5/17
 * @Version：
 *
 */
class SearchAdapter( data: List<Datas>) : BaseQuickAdapter<Datas, BaseViewHolder>(R.layout.home_list_item, data) {
    override fun convert(helper: BaseViewHolder, item: Datas?) {
        item ?: return
        @Suppress("DEPRECATION")
        helper.setText(R.id.homeItemTitle, Html.fromHtml(item.title))
            .setText(R.id.homeItemAuthor, item.author)
            .setText(R.id.homeItemType, item.chapterName)
            .addOnClickListener(R.id.homeItemType)
//            .setTextColor(R.id.homeItemType, context.resources.getColor(R.color.colorPrimary))
            .linkify(R.id.homeItemType)
            .setImageResource(
                R.id.homeItemLike,
                if (item.originId != 0 || item.collect) R.drawable.ic_favorite else R.drawable.ic_action_no_like
            )
            .addOnClickListener(R.id.homeItemLike)
    }
}