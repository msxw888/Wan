package com.example.wan.UI.Knowledgesys

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter

import com.example.wan.R
import com.example.wan.UI.Knowledgesys.vm.KnowViewModelFactory
import com.example.wan.UI.Knowledgesys.vm.KnowledgeViewModel
import com.example.wan.UI.account.vm.AccountViewModel
import com.example.wan.UI.account.vm.AccountViewModelFactory
import com.example.wan.UI.main.adapter.HomeAdapter
import com.example.wan.UI.webview.WebViewActivity
import com.example.wan.base.BaseFragment
import com.example.wan.bean.Article
import com.example.wan.context.UserContext
import com.example.wan.context.collect.CollectListener
import com.example.wan.toast
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.knowledge_artical_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class KnowledgeArticalFragment : BaseFragment(), KodeinAware {


    override val kodein: Kodein by closestKodein()
    val knowviewModelFactory : KnowViewModelFactory by instance()
    val accountViewModelFactory : AccountViewModelFactory by instance()

    private var page: Int = 0

    // 当前选中的 tab  索引  默认是 0 选中第一个
    private var tabIndex: Int = 0


    private lateinit var mArticleAdapter : HomeAdapter

    private lateinit var viewModel: KnowledgeViewModel

    private lateinit var accountViewModel: AccountViewModel

    private var datas = mutableListOf<Article>()
    /**
     *  标志位, 用于判断是否切换了 tab
     *  如果是切换了 tab  就重新设置 新的数据
     *  否则  就 添加数据
     */
    private var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this,knowviewModelFactory).get(KnowledgeViewModel::class.java)

        accountViewModel = ViewModelProviders.of(this,accountViewModelFactory).get(AccountViewModel::class.java)
        initdata()
    }

    // 获取 初始化 传入的 ids、titles
    private val titles: ArrayList<String>? by lazy { arguments?.getStringArrayList("titles") }
    private val ids: ArrayList<Int>? by lazy { arguments?.getIntegerArrayList("ids") }

    companion object {
        fun getNewInstance(ids: ArrayList<Int>, titles: ArrayList<String>): Fragment {
            val bundle = Bundle()
            bundle.putIntegerArrayList("ids", ids)
            bundle.putStringArrayList("titles", titles)
            val articleFragment = KnowledgeArticalFragment()
            articleFragment.arguments = bundle
            return articleFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.knowledge_artical_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        dadaObserve()
    }

    private fun initdata() {
        page = 0
        // 默认获取 一级菜单 二级菜单 第一个
        viewModel.getArticle(getCurrentCid(0), page)
    }

    override fun initView() {
        tabRvArticle.layoutManager = LinearLayoutManager(activity)
        mArticleAdapter = HomeAdapter(datas)

        mArticleAdapter.run {
            onItemClickListener = monItemClickListener
            onItemChildClickListener = this@KnowledgeArticalFragment.onItemChildClickListener
            setOnLoadMoreListener(onRequestLoadMoreListener,tabRvArticle)
        }
        tabRvArticle.adapter = mArticleAdapter

        //刷新数据监听器
        tabswipeRefresh.setOnRefreshListener { refreshData() }

        initSecondTreeTab()
    }

    private fun refreshData() {
        initdata()
    }

    private fun dadaObserve() {
        viewModel.mTreeArticleData.observe(this, Observer { it ->
            it?.let {
                if (flag) {
                    // 如果是切换了 tab  就重新设置 新的数据
                    // 后续的 加载更多就直接 添加数据
                    // 例如  开发环境 -> AndroidStudio 相关 切换至 gradle 就重新设置数据
                    mArticleAdapter.setNewData(it.data.datas)
                    flag = false
                } else {
                    addData(it.data.datas)
                }
            }
        })
        accountViewModel.mLikeData.observe(this, Observer {
            activity?.toast("收藏成功")
        })
        accountViewModel.mRequestCollectData.observe(this, Observer {
            activity?.toast("取消收藏成功")
        })
    }
    private fun initSecondTreeTab() {

        titles?.forEach { tabSecondTree.addTab(tabSecondTree.newTab().setText(it)) }

        tabSecondTree.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}

            override fun onTabUnselected(tab: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                flag = true
                tabIndex = tab?.position ?: 0
                viewModel.getArticle(getCurrentCid(tabIndex), 0)
            }
        })
    }
    private fun getCurrentCid(position: Int): Int {
        return ids?.let {
            // 判断 是否有 数据
            if (it.isNotEmpty()) it[position] else -1
        } ?: -1
    }

    private var onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        viewModel.getArticle(getCurrentCid(tabIndex), ++page)
    }

    /**
     * 条目点击监听器
     */
    private val monItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.isNotEmpty()){
            Intent(activity, WebViewActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY,datas[position].link)
                putExtra(Constant.CONTENT_ID_KEY,datas[position].id)
                putExtra(Constant.CONTENT_TITLE_KEY,datas[position].title)
                startActivity(this)
            }
        }
    }

    /**
     * 收藏
     */
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
//        if (datas.isNotEmpty()){
//            val data = datas[position]
        val article = mArticleAdapter.getItem(position)
        article?.let {
            when(view.id){
                R.id.homeItemLike ->{
                    UserContext.instance.collect(activity, position, object: CollectListener {
                        override fun collect(position: Int) {
                            val collect = it.collect
                            // 发起 收藏/取消收藏  请求
                            if (collect){
                                accountViewModel.unCollect(it.id)
                            }
                            else{
                                accountViewModel.Collect(it.id)
                            }
                            it.collect = !collect
                            mArticleAdapter.setData(position, it)
                        }
                    })
                }
            }
        }
    }

    fun addData(articleList: List<Article>) {

        // 如果为空的话，就直接 显示加载完毕
        if (articleList.isEmpty()) {
            mArticleAdapter.loadMoreEnd()
            return
        }

        // 如果是 下拉刷新 直接设置数据
        if (tabswipeRefresh.isRefreshing) {
            tabswipeRefresh.isRefreshing = false
            mArticleAdapter.setNewData(articleList)
            mArticleAdapter.loadMoreComplete()
            return
        }

        // 否则 添加新数据
        mArticleAdapter.addData(articleList)
        mArticleAdapter.loadMoreComplete()
    }

}
