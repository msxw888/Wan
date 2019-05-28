package com.example.wan.UI.Knowledgesys

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer

import com.example.wan.R
import com.example.wan.UI.Knowledgesys.adapter.TopTreeAdapter
import com.example.wan.base.BaseFragment
import com.example.wan.bean.TopTreeRsp
import kotlinx.android.synthetic.main.knowledge_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class KnowledgeFragment : BaseFragment(),KodeinAware{

    private val titles by lazy { arrayListOf<String>() }
    private val fragments by lazy { arrayListOf<Fragment>() }

//    companion object {
//        fun newInstance() = KnowledgeFragment()
//    }

    override val kodein: Kodein by closestKodein()
    val knowviewModelFactory : KnowViewModelFactory by instance()

    private lateinit var viewModel: KnowledgeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.knowledge_fragment, container, false)
    }


    override fun initView() {
        viewModel = ViewModelProviders.of(this,knowviewModelFactory).get(KnowledgeViewModel::class.java)
        // 关联 viewPage 与 TabLayout
        tabTopTree.setupWithViewPager(viewpageContent)
        viewModel.getTree()

        dataObserver()
    }

    fun dataObserver() {
       viewModel.mTreeData.observe(this, Observer {
           it?.let { initSystemData(it.data)
               Log.e("ftest", it.data.toString())
           }
       })
    }

    private fun initSystemData(data: List<TopTreeRsp>) {
        initTitle(data)
        initFragment(data)
        viewpageContent.adapter = TopTreeAdapter(childFragmentManager, titles, fragments)
    }

    private fun initTitle(topTreeList: List<TopTreeRsp>) {
        for (topTree in topTreeList) {
            titles.add(topTree.name)
        }
    }

    private fun initFragment(topTreeList: List<TopTreeRsp>): List<Fragment> {

        // 一级菜单
        for (topTreeRsp in topTreeList) {

            val ids = arrayListOf<Int>()
            val secondTreeTitles = arrayListOf<String>()

            // 二级菜单
            for (child in topTreeRsp.children) {
                ids.add(child.id)
                secondTreeTitles.add(child.name)
            }
            fragments.add(KnowledgeArticalFragment.getNewInstance(ids, secondTreeTitles))
        }
        return fragments
    }
}
