package com.example.wan.UI.Search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import com.example.wan.R
import com.example.wan.base.BaseActivity
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : BaseActivity() {
    /**
     * SearView
     */
    private var searchView: SearchView? = null

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
}
