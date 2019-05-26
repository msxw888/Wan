package com.example.wan.UI.like

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter

import com.example.wan.R
import com.example.wan.State.loginState
import com.example.wan.UI.account.AccountViewModel
import com.example.wan.UI.account.AccountViewModelFactory
import com.example.wan.UI.account.LoginActivity
import com.example.wan.UI.view.HorizontalRecyclerView
import com.example.wan.UI.webview.WebViewActivity
import com.example.wan.adapter.HomeAdapter
import com.example.wan.adapter.SearchAdapter
import com.example.wan.base.BaseFragment
import com.example.wan.bean.Datas
import com.example.wan.context.UserContext
import com.example.wan.toast
import kotlinx.android.synthetic.main.layout_recycleview.*
import kotlinx.android.synthetic.main.like_fragment.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class LikeFragment : BaseFragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val accountViewModelFactory: AccountViewModelFactory by instance()
    private val viewModelFactory : LikeViewModelFactory by instance()

    companion object {
        fun newInstance() = LikeFragment()
    }

    private lateinit var viewModel: LikeViewModel

    private lateinit var accountViewModel : AccountViewModel

    private var datas = mutableListOf<Datas>()

    private val madapter by lazy {
        HomeAdapter(datas)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.like_fragment, container, false)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this,viewModelFactory).get(LikeViewModel::class.java)

        accountViewModel = ViewModelProviders.of(this,accountViewModelFactory).get(AccountViewModel::class.java)
        // TODO: Use the ViewModel

        getcollectData()
        initView()

        recycle_like.run {
            layoutManager = LinearLayoutManager(activity)
            adapter = madapter
        }
        madapter.run {
            bindToRecyclerView(recycle_like)
            setEnableLoadMore(false)
            onItemClickListener = monItemClickListener
            onItemChildClickListener = this@LikeFragment.onItemChildClickListener
            setOnLoadMoreListener({
                val page = madapter.data.size / 20
                viewModel.getCollectList(page)
            }, recycle_like)
//            setEmptyView(R.layout.fragment_home_empty)
        }

        viewModel.collectList.observe(this, Observer {
            if (it.errorCode==0){
                addData(it.data.datas)
            }
            else
                UserContext.instance.login(activity)
        })
        accountViewModel.mLoginData.observe(this, Observer {
            if (it.state) {
                refreshData()
            }
            else
                UserContext.instance.login(activity)
        })

        swipe_refresh_like.setOnRefreshListener { refreshData() }
    }

    fun initView() {

    }

    private fun refreshData() {
        getcollectData()
    }

    private fun getcollectData() {
        if(UserContext.instance.isLogin){
            viewModel.getCollectList()
        }
        else{
            UserContext.instance.login(activity)
        }
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
     * 添加数据
     */
    fun addData(articleList: List<Datas>) {
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
