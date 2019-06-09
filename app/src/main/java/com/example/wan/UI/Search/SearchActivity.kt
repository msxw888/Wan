package com.example.wan.UI.Search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.wan.R
import com.example.wan.UI.Search.adapter.SearchAdapter
import com.example.wan.UI.Search.vm.SearchViewModel
import com.example.wan.UI.Search.vm.SearchViewModelFactory
import com.example.wan.UI.account.vm.AccountViewModel
import com.example.wan.UI.account.vm.AccountViewModelFactory
import com.example.wan.UI.webview.WebViewActivity
import com.example.wan.base.BaseActivity
import com.example.wan.bean.Article
import com.example.wan.context.UserContext
import com.example.wan.context.collect.CollectListener
import com.example.wan.toast
import kotlinx.android.synthetic.main.activity_search.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class SearchActivity : BaseActivity(),KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val searchViewModelFactory: SearchViewModelFactory by instance()
    private val accountViewModelFactory : AccountViewModelFactory by instance()

    private lateinit var viewModel : SearchViewModel

    private lateinit var accountViewModel : AccountViewModel

    /**
     * SearView
     */
    private var searchView: SearchView? = null

    /**
     * Search key
     */
    private var searchKey: String? = null

    /**
     * Data List
     */
    private var datas = mutableListOf<Article>()

    /**
     * adapter
     */
    private val searchAdapter: SearchAdapter by lazy {
        SearchAdapter(this, datas)
    }

    override fun setLayoutId(): Int = R.layout.activity_search

    override fun initView() {
        super.initView()
        search_toolbar.run {
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProviders.of(this@SearchActivity,searchViewModelFactory).get(SearchViewModel::class.java)
        accountViewModel = ViewModelProviders.of(this,accountViewModelFactory).get(AccountViewModel::class.java)

        search_recyclerView.run {
            layoutManager = LinearLayoutManager(this@SearchActivity)
            adapter = searchAdapter
        }
        searchAdapter.run {
            bindToRecyclerView(search_recyclerView)
            setOnLoadMoreListener({
                val page = searchAdapter.data.size / 20 + 1
                searchKey?.let {
                    viewModel.getSearchList(page, it)
                }
            }, search_recyclerView)
            onItemClickListener = this@SearchActivity.onItemClickListener
            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
//            setEmptyView(R.layout.fragment_home_empty)
        }
        viewModel.searchdata.observe(this, Observer {
            it.data.datas.let {
                addsearchData(it)
            }
            swipeRefreshLayout.isRefreshing = false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_search, menu)
        // get SearchView
        searchView = menu?.findItem(R.id.menuSearch)?.actionView as SearchView
        // init SearchView
        searchView?.init(1920, false, onQueryTextListener = onQueryTextListener)
        searchKey?.let {
            searchView?.setQuery(it, true)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onBackPressed() = finish()

    private val onQueryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                searchKey = it
                swipeRefreshLayout.isRefreshing = true
                searchAdapter.setEnableLoadMore(false)
                viewModel.getSearchList(k = it)
            } ?: let {
                swipeRefreshLayout.isRefreshing = false
                toast(getString(R.string.search_not_empty))
            }
            searchView?.clearFocus()
            return false
        }
        override fun onQueryTextChange(newText: String?): Boolean  = false
    }

    /**
     * 初始化 SearchView
     * kt拓展函数使用
     */
    private fun SearchView.init(
        sMaxWidth: Int = 0,
        sIconified: Boolean = false,
        isClose: Boolean = false,
        onQueryTextListener: SearchView.OnQueryTextListener
    ) = this.run {
        if (sMaxWidth != 0) {
            maxWidth = sMaxWidth
        }
        // false open
        isIconified = sIconified
        // not close
        if (!isClose) {
            // open
            onActionViewExpanded()
        }
        // search listener
        setOnQueryTextListener(onQueryTextListener)
    }
    /**
     * rv条目点击监听器
     */
    private val onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (datas.isNotEmpty()){
            Intent(this, WebViewActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY,datas[position].link)
                putExtra(Constant.CONTENT_TITLE_KEY,datas[position].title)
                startActivity(this)
            }
        }
        val article = searchAdapter.getItem(position)

        article?.let {
            Intent(this, WebViewActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY,it.link)
                putExtra(Constant.CONTENT_TITLE_KEY,it.title)
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
            val article = searchAdapter.getItem(position)
            article?.let {
                when(view.id){
                    R.id.homeItemLike ->{
                        UserContext.instance.collect(this, position, object: CollectListener {
                            override fun collect(position: Int) {
                                Log.d("LST", "position=$position")
                                val collect = it.collect
                                it.collect = !collect
                                searchAdapter.setData(position, it)
                                // 发起 收藏/取消收藏  请求
//                            if (collect) viewModel.unCollect(data.id) else viewModel.collect(data.id)
                                if (collect)
                                    accountViewModel.unCollect(it.id)
                                else
                                    accountViewModel.Collect(it.id)
                            }
                        })
                    }
                }
            }
//        }
    }

    /**
     * 添加数据
     */
    fun addsearchData(articleList: List<Article>) {

        // 如果为空的话，就直接 显示加载完毕
        if (articleList.isEmpty()) {
            searchAdapter.loadMoreEnd()
            swipeRefreshLayout.isRefreshing = false
            return
        }
        // 如果是下拉刷新 直接设置数据
        if (swipeRefreshLayout.isRefreshing) {
            swipeRefreshLayout.isRefreshing = false
            searchAdapter.setNewData(articleList)
            searchAdapter.loadMoreComplete()
            return
        }
        // 否则 添加新数据
        searchAdapter.addData(articleList)
        searchAdapter.loadMoreComplete()
    }
}
