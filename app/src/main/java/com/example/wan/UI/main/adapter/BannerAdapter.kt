package com.example.wan.UI.main.adapter

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.wan.R
import com.example.wan.bean.BannerResponse

class BannerAdapter(val context: Context?, datas: MutableList<BannerResponse.Data>) :
    BaseQuickAdapter<BannerResponse.Data, BaseViewHolder>(R.layout.banner_item, datas) {
    override fun convert(helper: BaseViewHolder, item: BannerResponse.Data?) {
        item ?: return
        helper.setText(R.id.banner_title, item.title.trim())
        val imageView = helper.getView<ImageView>(R.id.banner_image)
        context?.let { Glide.with(it).load(item.imagePath).placeholder(R.drawable.image).into(imageView) }
    }
}

