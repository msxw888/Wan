package com.example.wan

import Constant
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.example.wan.State.LoginState
import com.example.wan.UI.Search.SearchActivity
import com.example.wan.UI.account.LoginActivity
import com.example.wan.UI.account.vm.AccountViewModel
import com.example.wan.UI.account.vm.AccountViewModelFactory
import com.example.wan.UI.main.MainFragment
import com.example.wan.UI.main.vm.MainViewModelFactory
import com.example.wan.base.BaseActivity
import com.example.wan.base.Preference
import com.example.wan.context.UserContext
import io.flutter.embedding.android.FlutterActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_webview.*
import kotlinx.android.synthetic.main.layout_content.*
import kotlinx.android.synthetic.main.nav_head_main.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.generic.instance

class MainActivity : BaseActivity(), KodeinAware {

//    override fun onWindowFocusChanged(hasFocus: Boolean) {
//        super.onWindowFocusChanged(hasFocus)
//        Debug.stopMethodTracing()
//    }

    private var lastTime: Long = 0

    private val navController: NavController by lazy { Navigation.findNavController(this,R.id.nav_host_fragment) }

    override val kodein: Kodein by lazy { (applicationContext as KodeinAware).kodein }
    private val mainviewModelFactory: MainViewModelFactory by instance()
    private val accountViewModelFactory: AccountViewModelFactory by instance()


    private lateinit var accountViewModel: AccountViewModel

    private val fragmentManager by lazy {
        supportFragmentManager
    }

    private var mainFragment: MainFragment? = null

    private lateinit var navigationname: TextView


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
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        accountViewModel = ViewModelProviders.of(this,accountViewModelFactory)
            .get(AccountViewModel::class.java)
        //设置Toolbar标题
        setToolBar(toolbar_main,getString(R.string.app_name))

        //设置抽屉导航图标、按钮有旋转特效
        val toggle = ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer.addDrawerListener(toggle)
        toggle.syncState()

        //jetpack的navigation
//        navController = Navigation.findNavController(this,R.id.nav_host_fragment)
//        navController.navigatorProvider

        /**
         * 自定义Nav
//         */
//        // get fragment
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)!!
//        // setup custom navigator
//        val navigator = MyNav(this, navHostFragment.childFragmentManager, R.id.nav_host_fragment)
//        navController.navigatorProvider += navigator
//        // set navigation graph
//        navController.setGraph(R.navigation.nav_graph)

        bottomNavigation?.setupWithNavController(navController)
//        setupActionBarWithNavController(this,navController)

        dadaObserve()
        initNavigation()
        initDrawerLayout()

    }

    override fun onStart() {
        super.onStart()

    }

    override fun onResume() {
        super.onResume()
        /**
         * 其他页面login之后可以更新
         */
        val mTvName :TextView= nav_view.getHeaderView(0).findViewById(R.id.mTvName)
        if (UserContext.instance.isLogin) {
            mTvName.let {
                it.text = user
            }
        }
    }

    private fun dadaObserve() {
        accountViewModel.mLoginData.observe(this, Observer {
            navigationname.text = it.usern
        })
    }

    private fun initNavigation() {
        navigationname = nav_view.getHeaderView(0).findViewById(R.id.mTvName)
        navigationname.run {
            text = if (UserContext.instance.isLogin) {
                user
            } else {
                getString(R.string.goto_login)
            }
            setOnClickListener {
                if (!UserContext.instance.isLogin) {
                    Intent(this@MainActivity, LoginActivity::class.java).run {
                        startActivityForResult(this, Constant.MAIN_REQUEST_CODE)
                    }
                }
            }
        }

    }

    private fun initDrawerLayout() {
        nav_view.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_logout ->{
                        UserContext.instance.logoutSuccess()
                        mTvName.text = getString(R.string.goto_login)
                        accountViewModel.mLoginData.value = LoginState(getString(R.string.goto_login), false)
                        mainFragment?.refreshData()
                        navController.navigate(R.id.mainFragment_dest)
                        toast("已退出登录")
                        true
                }

                R.id.nav_flutter ->{
                    startActivity(
                        FlutterActivity.createDefaultIntent(this)
                    )
                    true
                }
                else -> false
            }
        }
        // 关闭侧边栏
        drawer.closeDrawers()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
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


    /**
     * loginactivity登陆成功后刷新Drawlayout信息
     */
    // TODO: 2019/5/25 可修改为loginsuccess回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            Constant.MAIN_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
//                    val navigation :TextView = nav_view.getHeaderView(0).findViewById(R.id.mTvName)
////                    navigationname.text = data?.getStringExtra(Constant.CONTENT_TITLE_KEY)
//                    navigation.run {
//                        text = data?.getStringExtra(Constant.CONTENT_TITLE_KEY)
//                    }
                    mTvName.text = data?.getStringExtra(Constant.CONTENT_TITLE_KEY)
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
        if (currentTime - lastTime < 1 * 1000) {
            super.onBackPressed()

            finish()
        } else {
            toast(getString(R.string.exit_app_tips))
            lastTime = currentTime
        }
    }


    /**
     * 暂不使用，暂时还是使用navcontrl
     * 但是navcontrl的缺点是每次切换都是replace，需要重复请求数据，多消耗了流量
     * 下面这个是使用hide
     */
    // 当前显示的 fragment
    private lateinit var mCurrentFragment: Fragment
    // 复用 fragment
    private fun goTo(to: Fragment) {
        if (mCurrentFragment != to) {
            val transaction = supportFragmentManager.beginTransaction()
            if (to.isAdded)
                transaction.hide(mCurrentFragment).show(to)
            else
                transaction.hide(mCurrentFragment).add(R.id.content, to)
            transaction.commit()
            mCurrentFragment = to
        }
    }
}
