package com.example.wan.UI.Knowledgesys.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/5/28
 * @Version：
 *
 */
class TopTreeAdapter(fm :FragmentManager,val title:List<String>,val fragments: List<Fragment>)
    :FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
    override fun getItem(position: Int): Fragment  =fragments[position]

    override fun getCount(): Int  = fragments.size

    override fun getPageTitle(position: Int) = title[position]
}