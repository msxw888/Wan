package com.example.wan.base

import android.app.Application
import com.example.wan.UI.Knowledgesys.vm.KnowViewModelFactory
import com.example.wan.UI.Search.vm.SearchViewModelFactory
import com.example.wan.UI.account.AccountRepository
import com.example.wan.UI.account.vm.AccountViewModelFactory
import com.example.wan.UI.like.vm.LikeViewModelFactory
import com.example.wan.UI.main.MainFragment
import com.example.wan.UI.main.vm.MainViewModelFactory
import com.example.wan.repository.remote.RetrofitHelper
import com.example.wan.repository.Repository
import com.example.wan.repository.remote.NetworkDataimpl
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import java.util.concurrent.Executor

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/3/31
 * @Version：
 *
 */
class MyApplication : Application(),KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@MyApplication))

        bind<Repository>() with singleton { Repository.newinstance(instance()) }
        bind() from provider { MainViewModelFactory(instance()) }
        bind<RetrofitHelper>() with singleton { RetrofitHelper.instance}
        bind<MainFragment>() with singleton { MainFragment() }
//        bind<API>() with singleton { API() }
        bind<Executor>() with singleton { Executor{instance()} }
        bind<NetworkDataimpl>() with singleton { NetworkDataimpl(instance()) }
        bind() from provider { AccountViewModelFactory(instance()) }
        bind<AccountRepository>() with singleton { AccountRepository(instance()) }
        bind() from provider { SearchViewModelFactory(instance()) }
        bind() from provider { LikeViewModelFactory(instance()) }
        bind() from provider { KnowViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        Preference.setContext(applicationContext)
    }
}