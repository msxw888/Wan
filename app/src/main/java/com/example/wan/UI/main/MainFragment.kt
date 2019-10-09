package com.example.wan.UI.main

import Constant
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.wan.R
import com.example.wan.UI.account.vm.AccountViewModel
import com.example.wan.UI.account.vm.AccountViewModelFactory
import com.example.wan.UI.main.adapter.BannerAdapter
import com.example.wan.UI.main.adapter.HomeAdapter
import com.example.wan.UI.main.vm.MainViewModel
import com.example.wan.UI.main.vm.MainViewModelFactory
import com.example.wan.UI.view.HorizontalRecyclerView
import com.example.wan.UI.webview.WebViewActivity
import com.example.wan.base.BaseFragment
import com.example.wan.bean.Article
import com.example.wan.bean.BannerResponse
import com.example.wan.context.UserContext
import com.example.wan.context.collect.CollectListener
import com.example.wan.toast
import kotlinx.android.synthetic.main.layout_recycleview.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class MainFragment : BaseFragment(), KodeinAware {
    private var currentIndex = 0

    /**
     * 依赖注入MainViewModelFactory
     */
    override val kodein by closestKodein()


    private val viewModelFactory: MainViewModelFactory by instance()
    private val accountViewModelFactory: AccountViewModelFactory by instance()
    /**
     * Viewmodel
     */
    private lateinit var viewModel: MainViewModel
    private lateinit var accountViewModel: AccountViewModel

    /**
     * LinearLayoutManager
     * 设置recycleview横向
     */
    private val linearLayoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
    }

    /**
     * BannerRecycleview
     */
    private lateinit var bannerRecycleView: HorizontalRecyclerView

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

    private val bannerAdapter: BannerAdapter by lazy {
        BannerAdapter(activity, bannerDatas)
    }

    private var datas: List<Article> = mutableListOf()

    private val homeAdapter = HomeAdapter(datas)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mainView ?: let {
            mainView = inflater.inflate(R.layout.main_fragment, container, false)
            bannerRecycleView = LayoutInflater.from(activity).inflate(
                R.layout.banner,
                null
            ) as HorizontalRecyclerView
//            View.inflate(activity,R.layout.banner,null) as HorizontalRecyclerView
        }
        return mainView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        accountViewModel =
            ViewModelProviders.of(this, accountViewModelFactory).get(AccountViewModel::class.java)
        getdata()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initData()
        dadaObserve()
//        initAdapter()
//        initSwipeToRefresh()
    }

    override fun initView() {
        recycle_main.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = homeAdapter

        }
        bannerRecycleView.run {
            //在layoutmanger设置recycleview横向
            layoutManager = linearLayoutManager
            adapter = bannerAdapter
            bannerPagerSnap.attachToRecyclerView(this)
            requestDisallowInterceptTouchEvent(true)
            setOnTouchListener(onTouchListener)
            addOnScrollListener(onScrollListener)
        }
        homeAdapter.run {
            onItemClickListener = monItemClickListener
            onItemChildClickListener = this@MainFragment.onItemChildClickListener
            if (headerLayout == null) {
                addHeaderView(bannerRecycleView)
            }
            setOnLoadMoreListener(onRequestLoadMoreListener, recycle_main)
        }
        bannerAdapter.run {
            onItemClickListener = banneronItemClickListener
        }
        swipe_refresh.setOnRefreshListener { refreshData() }
//            setEmptyView(R.layout.fragment_home_empty)
        setRefreshView()
    }

    fun initData() {
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
    }

    private fun dadaObserve() {
        accountViewModel.mLikeData.observe(this, Observer {
            activity?.toast("收藏成功")
        })
        accountViewModel.mRequestCollectData.observe(this, Observer {
            activity?.toast("取消收藏成功")
        })
        viewModel.netstate.observe(this, Observer {
            activity?.toast("网络不好，刷新重试")
            swipe_refresh.isRefreshing = false
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()

    }


    /**
     * 从viewmodel获取数据
     */
    private fun getdata() {
        viewModel.getfirstList()
        viewModel.getbannerList()
    }

    private fun setRefreshView() {
        swipe_refresh.isRefreshing = true
    }

    /**
     * 下拉刷新数据
     */
    fun refreshData() {
        getdata()
    }

    private var onScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            when (newState) {
                RecyclerView.SCROLL_STATE_IDLE -> {
                    currentIndex = linearLayoutManager.findFirstVisibleItemPosition()
                    startSwitchJob()
                }
            }
        }
    }

    private var onTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_MOVE -> {
                cancelSwitchJob()
            }
        }
        false
    }

    /**
     * 加載更多
     */
    private var onRequestLoadMoreListener = BaseQuickAdapter.RequestLoadMoreListener {
        val page = homeAdapter.data.size / 20
        viewModel.gethomelist(page)
    }

    /**
     * 添加首页数据
     * @param articleList List<Article>
     */
    fun addHomeData(articleList: List<Article>) {
        // 如果为空的话，就直接 显示加载完毕
        if (articleList.isEmpty()) {
            homeAdapter.loadMoreEnd()
            return
        }
        // 如果是 下拉刷新 直接设置数据
        if (swipe_refresh.isRefreshing) {
            swipe_refresh.isRefreshing = false
            homeAdapter.setNewData(articleList)
            homeAdapter.loadMoreComplete()
            return
        }
        // 否则 添加新数据
        homeAdapter.addData(articleList)
        homeAdapter.loadMoreComplete()
    }

    private fun addBannerData(it: List<BannerResponse.Data>?) {
        it?.let { it1 ->
            if (bannerAdapter.data.isEmpty())
                bannerAdapter.addData(it1)
        }
    }

    /**
     * rv条目点击监听器
     */
    private val monItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        //        if (datas.isNotEmpty()){
        val article = homeAdapter.getItem(position)
        article?.let {
            Intent(activity, WebViewActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, it.link)
                putExtra(Constant.CONTENT_ID_KEY, it.id)
                putExtra(Constant.CONTENT_TITLE_KEY, it.title)
                startActivity(this)
            }
        }
    }
    /**
     * banner点击监听器
     */
    private val banneronItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
        if (bannerDatas.isNotEmpty()) {
            Intent(activity, WebViewActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, bannerDatas[position].url)
                putExtra(Constant.CONTENT_ID_KEY, bannerDatas[position].id)
                putExtra(Constant.CONTENT_TITLE_KEY, bannerDatas[position].title)
                startActivity(this)
            }
        }
    }

    /**
     *收藏按钮click监听
     */
    private val onItemChildClickListener =
        BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
            //        if (datas.isNotEmpty()) {
//            val data = datas[position]
            val article = homeAdapter.getItem(position)
            article?.let {
                when (view.id) {
                    R.id.homeItemLike -> {
                        UserContext.instance.collect(context, position, object : CollectListener {
                            override fun collect(position: Int) {
                                Log.d("LST", "position=$position")
                                val collect = it.collect


                                // 发起 收藏/取消收藏  请求
//                            if (collect) viewModel.unCollect(data.id) else viewModel.collect(data.id)
                                if (collect)
                                    accountViewModel.unCollect(it.id)
                                else
                                    accountViewModel.Collect(it.id)
                                it.collect = !collect
                                homeAdapter.setData(position, it)
                            }
                        })
//                    if (UserContext.instance.isLogin) {
//                        val collect = data.collect
//                        data.collect = !collect
//                        homeAdapter.setData(position, data)
////                        viewModel.collectArticle(data.id, !collect)
//                    } else {
//                        Intent(activity, LoginActivity::class.java).run {
//                            startActivity(this)
//                        }
//                        activity?.toast(getString(R.string.login_please_login))
//                    }
                    }
                }
            }
        }


    /**
     * 获得Banner切换job
     * @return Job
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
        if (isActive) {
            cancel()
        }
    }

    companion object {
        fun newInstance() = MainFragment()
        private const val BANNER_TIME = 4000L
    }

    override fun onDestroy() {

        bannerSwitchJob.run { cancelSwitchJob() }
        super.onDestroy()
    }
}



