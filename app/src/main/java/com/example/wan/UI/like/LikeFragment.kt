package com.example.wan.UI.like

import Constant
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.example.wan.R
import com.example.wan.UI.account.vm.AccountViewModel
import com.example.wan.UI.account.vm.AccountViewModelFactory
import com.example.wan.UI.like.vm.LikeViewModel
import com.example.wan.UI.like.vm.LikeViewModelFactory
import com.example.wan.UI.main.adapter.HomeAdapter
import com.example.wan.UI.webview.WebViewActivity
import com.example.wan.base.BaseFragment
import com.example.wan.bean.Article
import com.example.wan.context.UserContext
import com.example.wan.context.collect.CollectListener
import com.example.wan.toast
import kotlinx.android.synthetic.main.like_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class LikeFragment : BaseFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private val viewModelFactory: LikeViewModelFactory by instance()

    companion object {
        fun newInstance() = LikeFragment()

    }

    private lateinit var viewModel: LikeViewModel

    private lateinit var accountViewModel: AccountViewModel

    private var likedatas = mutableListOf<Article>()

    private val madapter by lazy {
        HomeAdapter(likedatas)
    }

    private var page = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.like_fragment, container, false)
    }



    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recycle_like.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = madapter
        }
        if (recycle_like.adapter==null){
            madapter.bindToRecyclerView(recycle_like)
        }
        madapter.run {
            setEnableLoadMore(false)
            onItemClickListener = monItemClickListener
            onItemChildClickListener = this@LikeFragment.onItemChildClickListener
            setOnLoadMoreListener({
                viewModel.getCollectList(++page)
            }, recycle_like)
            setEmptyView(R.layout.fragment_home_empty)
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LikeViewModel::class.java)

        accountViewModel = ViewModelProviders.of(this, accountViewModelFactory).get(AccountViewModel::class.java)
        // TODO: Use the ViewModel
        refreshData()
        dadaObserve()

    }


    override fun initView() {

    }

    override fun networkError() {
        super.networkError()
        viewModel.netstate.observe(this, Observer {
            swipe_refresh_like.isRefreshing = false
            activity?.toast("网络不好，刷新重试")
        })
    }

    private fun dadaObserve() {
        viewModel.collectList.observe(this, Observer {
            if (it.errorCode == 0) {
                addData(it.data.datas)
            } else
                UserContext.instance.login(activity)
        })
        accountViewModel.mLoginData.observe(this, Observer {
            if (it.state) {
                refreshData()
            } else
                UserContext.instance.login(activity)
        })

        accountViewModel.mLikeData.observe(this, Observer {
            activity?.toast("收藏成功")
        })
        accountViewModel.mRequestCollectData.observe(this, Observer {
            activity?.toast("取消收藏成功")
        })

        swipe_refresh_like.setOnRefreshListener { refreshData() }
    }


    private fun refreshData() {
        if (UserContext.instance.isLogin){
            getcollectData()
        }else{
            activity?.toast(getString(R.string.login_please_login))
            swipe_refresh_like.isRefreshing=false
        }
    }

    private fun getcollectData() {
        if (UserContext.instance.isLogin) {
            swipe_refresh_like.isRefreshing = true
            page = 0
            viewModel.getCollectList()
        } else {
//            findNavController().navigate(R.id.mainFragment_dest)
            UserContext.instance.login(activity)
        }

    }



    /**
     * 条目点击监听器
     */
    private val monItemClickListener = BaseQuickAdapter.OnItemClickListener { _, _, position ->
            val likearticle = madapter.getItem(position)
        likearticle?.let {
            Intent(activity, WebViewActivity::class.java).run {
                putExtra(Constant.CONTENT_URL_KEY, it.link)
                putExtra(Constant.CONTENT_ID_KEY, it.id)
                putExtra(Constant.CONTENT_TITLE_KEY, it.title)
                startActivity(this)
            }
        }
    }

    /**
     *收藏按钮click监听
     */
    private val onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { _, view, position ->
        //        if (likedatas.isNotEmpty()) {
//            val data = likedatas[position]
        val article = madapter.getItem(position)
        article?.let {
            when (view.id) {
                R.id.homeItemLike -> {
                    UserContext.instance.collect(context, position, object : CollectListener {
                        override fun collect(position: Int) {
//                            val collect = data.collect
//                            data.collect = !collect
//                            madapter.setData(position, data)
                            accountViewModel.removeCollectArticle(it.id, it.originId)
                            madapter.remove(position)

                        }
                    })
//
//                    if (UserContext.instance.isLogin) {
//                        val collect = data.collect
//                        data.collect = !collect
//                        madapter.setData(position, data)
//                        madapter.remove(position)
//                        accountViewModel.removeCollectArticle(data.id, !collect)
//                    } else {
//                        Intent(activity, LoginActivity::class.java).run {
//                            startActivity(this)
//                        }
//                        activity?.toast(getString(R.string.login_please_login))
//                    }
//                }
                }
            }
        }
    }

    /**
     * 添加数据
     */
    fun addData(articleList: List<Article>) {
        // 如果为空的话，就直接 显示加载完毕
        if (articleList.isEmpty()) {
            madapter.loadMoreEnd()
            swipe_refresh_like.isRefreshing = false
            return
        }
        // 如果是下拉刷新 直接设置数据
        if (swipe_refresh_like.isRefreshing) {
            swipe_refresh_like.isRefreshing = false
            madapter.setNewData(articleList)
            madapter.loadMoreComplete()
            return
        }
        // 否则 添加新数据
        madapter.addData(articleList)
        madapter.loadMoreComplete()
    }
}


