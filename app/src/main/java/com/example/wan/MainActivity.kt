package com.example.wan

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.AppCompatButton
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import com.example.wan.State.loginState
import com.example.wan.UI.Search.SearchActivity
import com.example.wan.UI.account.AccountViewModel
import com.example.wan.UI.account.AccountViewModelFactory
import com.example.wan.UI.account.LoginActivity
import com.example.wan.UI.main.MainFragment
import com.example.wan.UI.main.MainViewModel
import com.example.wan.UI.main.MainViewModelFactory
import com.example.wan.base.BaseActivity
import com.example.wan.base.Preference
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.layout_content.*
import kotlinx.android.synthetic.main.nav_head_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : BaseActivity(), KodeinAware {

    private var lastTime: Long = 0

    private lateinit var navController: NavController

    override val kodein by closestKodein()
    private val mainviewModelFactory: MainViewModelFactory by instance()
    private val accountViewModelFactory:AccountViewModelFactory by instance()

    private lateinit var accountViewModel: AccountViewModel

    private val fragmentManager by lazy {
        supportFragmentManager
    }

    private var mainFragment: MainFragment? = null

    private lateinit var navigationname:TextView

    /**
     * check login from SharedPreferences
     */
    private val isLogin: Boolean by Preference(Constant.LOGIN_KEY, false)
    /**
     * local username
     */
    private val user: String by Preference(Constant.USERNAME_KEY, "")


    /**
     * 设置界面id
     */
    override fun setLayoutId(): Int = R.layout.activity_main

    /**
     * 右上角的搜索图标按钮
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //设置Toolbar标题
        setToolBar(toolbar_main,getString(R.string.app_name))

        //设置导航图标、按钮有旋转特效
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        //jetpack的navigation
//        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
//        bottomNavigation.setupWithNavController(navController)
//        setupActionBarWithNavController(this,navController)

        accountViewModel = ViewModelProviders.of(this,accountViewModelFactory).get(AccountViewModel::class.java)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.content, MainFragment.newInstance())
                .commitNow()
        }
        initNavigation()
        initDrawerLayout()
    }

    private fun initNavigation() {
        navigationname = nav_view.getHeaderView(0).findViewById<TextView>(R.id.mTvName)
        navigationname.run {
            text = if (!isLogin) {
                getString(R.string.goto_login)
            } else {
                user
//                getString(R.string.not_login)
            }
            setOnClickListener {
                if (!isLogin) {
                    Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivityForResult(this, Constant.MAIN_REQUEST_CODE)
                    }
                }
            }
        }
        accountViewModel.mLoginData.observe(this, Observer {
            navigationname.text = it.usern
        })
    }

    private fun initDrawerLayout() {
        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_logout ->{
                    Preference.clear()
                    mTvName.text = getString(R.string.goto_login)
                    accountViewModel.mLoginData.value = loginState("点此登录",false)
                    mainFragment?.refreshData()
                    toast("已退出登录")
                    true
                }

                else -> false
            }
        }
        // 关闭侧边栏
        drawer.closeDrawers()
        true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            //将滑动菜单显示出来
            android.R.id.home -> {
                drawer.openDrawer(GravityCompat.START)
                return true
            }
            // 跳转到 搜索
            R.id.menuSearch -> {
                Intent(this, SearchActivity::class.java).run {
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Constant.MAIN_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    navigationname.text = data?.getStringExtra(Constant.CONTENT_TITLE_KEY)
                }
                mainFragment?.refreshData()
            }
            Constant.MAIN_LIKE_REQUEST_CODE -> mainFragment?.refreshData()
        }
    }

    override fun onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
            return
        }
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastTime < 2 * 1000) {
            super.onBackPressed()
            finish()
        } else {
            toast(getString(R.string.exit_app_tips))
            lastTime = currentTime
        }
    }
}
