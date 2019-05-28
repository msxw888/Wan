package com.example.wan.UI.Search

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wan.R
import com.example.wan.UI.Search.adapter.SearchAdapter
import com.example.wan.base.BaseActivity
import com.example.wan.bean.Article
import com.example.wan.toast
import kotlinx.android.synthetic.main.activity_search.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class SearchActivity : BaseActivity(),KodeinAware {
    override val kodein: Kodein by closestKodein()
    private val searchViewModelFactory: SearchViewModelFactory by instance()


    private lateinit var viewModel :SearchViewModel
    /**
     * SearView
     */
    private var searchView: SearchView? = null

    /**
     * Search key
     */
    private var searchKey: String? = null

    private var searchDatas = mutableListOf<Article>()

    private val searchAdapter : SearchAdapter by lazy {
        SearchAdapter(searchDatas)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this@SearchActivity,searchViewModelFactory).get(SearchViewModel::class.java)

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
//            onItemClickListener = this@SearchActivity.onItemClickListener
//            onItemChildClickListener = this@SearchActivity.onItemChildClickListener
//            setEmptyView(R.layout.fragment_home_empty)
        }
        viewModel.searchdata.observe(this, Observer {
            it.data.datas.let {
                addsearchData(it)
            }
            swipeRefreshLayout.isRefreshing = false
        })
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
