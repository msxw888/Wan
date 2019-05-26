package com.example.wan.UI.main

import Constant
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.wan.R
import com.example.wan.UI.account.LoginActivity
import com.example.wan.UI.view.HorizontalRecyclerView
import com.example.wan.UI.webview.WebViewActivity
import com.example.wan.adapter.BannerAdapter
import com.example.wan.adapter.HomeAdapter
import com.example.wan.base.Preference
import com.example.wan.bean.BannerResponse
import com.example.wan.bean.Datas
import com.example.wan.context.UserContext
import com.example.wan.toast
import kotlinx.android.synthetic.main.layout_recycleview.*
import kotlinx.coroutines.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MainFragment : Fragment() ,KodeinAware{

    private var currentIndex = 0


    /**
     * 依赖注入MainViewModelFactory
     */
    override val kodein by closestKodein()
    private val viewModelFactory : MainViewModelFactory by instance()

    /**
     * Viewmodel
     */
    private lateinit var viewModel: MainViewModel

    /**
     * LinearLayoutManager
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    /**
     * BannerRecycleview
     */
    private lateinit var bannerRecycleView:HorizontalRecyclerView

    /**
     * Banner switch job
     */
    private var bannerSwitchJob: Job? = null

    /**
     * Banner PagerSnapHelper
     */
    private val bannerPagerSnap: PagerSnapHelper by lazy {
        PagerSnapHelper()
    }

    private var mainView: View? = null

    private var bannerDatas = mutableListOf<BannerResponse.Data>()

    private val bannerAdapter :BannerAdapter by lazy {
        BannerAdapter(activity,bannerDatas)
    }

    private var datas: List<Datas> = mutableListOf()
    private val madapter = HomeAdapter(datas)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainView?:let{
            mainView = inflater.inflate(R.layout.main_fragment, container, false)
            bannerRecycleView = View.inflate(activity,R.layout.banner,null) as HorizontalRecyclerView
        }
        return mainView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(MainViewModel::class.java)
        getdata()
        bindUI()
//        initAdapter()
//        initSwipeToRefresh()
    }
//    private fun initAdapter() {
//
//        val adapter = HomePagingAdapter {
//           viewModel.retry()
//        }
//        recycle_main.apply {
//            layoutManager = LinearLayoutManager(context)
//            this.adapter = adapter
//        }
//        viewModel.posts.observe(this, Observer<PagedList<Datas?>> {
//            adapter.submitList(it)
//        })
//        viewModel.networkState.observe(this, Observer {
//            adapter.setNetworkState(it)
//        })
//    }

//    private fun initSwipeToRefresh() {
//        viewModel.refreshState.observe(this, Observer {
//            swipe_refresh.isRefreshing = it == NetworkState.LOADING
//        })
//        swipe_refresh.setOnRefreshListener {
//            viewModel.refresh()
//        }
//    }

    private fun bindUI() {
        recycle_main.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = madapter
        }
        bannerRecycleView.run {
            layoutManager = linearLayoutManager
            bannerPagerSnap.attachToRecyclerView(this)
            requestDisallowInterceptTouchEvent(true)
            setOnTouchListener(onTouchListener)
            addOnScrollListener(onScrollListener)
        }
        bannerAdapter.run {
            bindToRecyclerView(bannerRecycleView)
        }
        madapter.run {
            addHeaderView(bannerRecycleView)
            bindToRecyclerView(recycle_main)
            setEnableLoadMore(true)
            onItemClickListener = monItemClickListener
            onItemChildClickListener = this@MainFragment.onItemChildClickListener
            setOnLoadMoreListener(onRequestLoadMoreListener, recycle_main)
//            setEmptyView(R.layout.fragment_home_empty)
        }

        viewModel._homedata.observe(this, Observer { response ->
            response.data.datas.let {
                addHomeData(it)
            }
        })
        viewModel.bannerdata.observe(this, Observer {
            viewModel.bannerdata.value?.data.let {
                addBannerData(it)
                startSwitchJob()
            }
        })
        swipe_refresh.setOnRefreshListener { refreshData() }
    }

    /**
     * 从viewmodel获取数据
     */
    private fun getdata() {
        viewModel.getfirstList()
        viewModel.getbannerList()
    }

    /**
     * 下拉刷新数据
     */
    fun refreshData() {
        viewModel.getfirstList()
        viewModel.getbannerList()
    }

    private var onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when(newState){
                RecyclerView.SCROLL_STATE_IDLE->{
                    currentIndex = linearLayoutManager.findFirstVisibleItemPosition()
                    startSwitchJob()
                }
            }
        }
    }

    private var onTouchListener =  View.OnTouchListener { v, event ->
        when(event.action){
            MotionEvent.ACTION_MOVE ->{
                cancelSwitchJob()
            }
        }
        false
    }

    private var onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        val page = madapter.data.size / 20
        viewModel.gethomelist(page)
    }

    /**
     * 添加首页数据
     */
    fun addHomeData(articleList: List<Datas>) {
        // 如果为空的话，就直接 显示加载完毕
        if (articleList.isEmpty()) {
            madapter.loadMoreEnd()
            return
        }
        // 如果是 下拉刷新 直接设置数据
        if (swipe_refresh.isRefreshing) {
            swipe_refresh.isRefreshing = false
            madapter.setNewData(articleList)
            madapter.loadMoreComplete()
            return
        }
        // 否则 添加新数据
        madapter.addData(articleList)
        madapter.loadMoreComplete()
    }

    private fun addBannerData(it: List<BannerResponse.Data>?) {
        it?.let { it1 -> bannerAdapter.addData(it1) }
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
     *收藏按钮click监听
     */
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        if (datas.isNotEmpty()) {
            val data = datas[position]
            when (view.id) {
                R.id.homeItemLike -> {
                    if (UserContext.instance.isLogin) {
                        val collect = data.collect
                        data.collect = !collect
                        madapter.setData(position, data)
//                        viewModel.collectArticle(data.id, !collect)
                    } else {
                        Intent(activity, LoginActivity::class.java).run {
                            startActivity(this)
                        }
                        activity?.toast(getString(R.string.login_please_login))
                    }
                }
            }
        }
    }

    /**
     * 获得Banner切换job
     */
    private fun getBannerSwitchJob() = GlobalScope.launch {
        repeat(Int.MAX_VALUE) {
            if (bannerDatas.size == 0) {
                return@launch
            }
            delay(BANNER_TIME)
            currentIndex++
            val index = currentIndex % bannerDatas.size
            bannerRecycleView.smoothScrollToPosition(index)
            currentIndex = index
        }
    }

    /**
     * 恢复横幅切换开关
     */
    private fun startSwitchJob() = bannerSwitchJob?.run {
        if (!isActive) {
            bannerSwitchJob = getBannerSwitchJob().apply { start() }
        }
    } ?: let {
        bannerSwitchJob = getBannerSwitchJob().apply { start() }
    }

    /**
     * 取消滚动job
     */
    private fun cancelSwitchJob() = bannerSwitchJob?.run {
        if (isActive){
            cancel()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
        private const val BANNER_TIME = 4000L
    }
}



