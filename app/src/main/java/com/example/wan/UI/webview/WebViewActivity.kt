package com.example.wan.UI.webview

import android.widget.LinearLayout
import com.example.wan.R
import com.example.wan.base.BaseActivity
import com.example.wan.toHtml
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.activity_webview.*


class WebViewActivity : BaseActivity() {
    private lateinit var mAgentWeb: AgentWeb

    override fun setLayoutId(): Int {
        return R.layout.activity_webview
    }


    override fun initView() {
        toolbar.run {
            title = getString(R.string.loading)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { finish() }
        }
        val link = intent.getStringExtra("url")
        toolbar.title = intent.getStringExtra("title").toHtml()

        mAgentWeb = AgentWeb.with(this)
                // 传入 AgentWeb 父容器  mLlContent,  第二个参数是对应的 LinearLayout
                .setAgentWebParent(mLlContent, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()  // 默认进度条
                .createAgentWeb()
                .ready()
                .go(link)       // 加载 url

        val setting = mAgentWeb.agentWebSettings.webSettings
        // 设置 支持缩放
        setting.builtInZoomControls = true
    }

    override fun onBackPressed() {
        mAgentWeb.clearWebCache()
        mAgentWeb.destroy()
        finish()
    }
}