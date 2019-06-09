package com.example.wan.UI.account

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.wan.R
import com.example.wan.UI.account.vm.AccountViewModel
import com.example.wan.UI.account.vm.AccountViewModelFactory
import com.example.wan.base.BaseActivity
import com.example.wan.context.UserContext
import com.example.wan.toast
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.mTieAccount
import kotlinx.android.synthetic.main.activity_register.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class RegisterActivity : BaseActivity(),KodeinAware {
    override val kodein by closestKodein()
    private val viewModelFactory: AccountViewModelFactory by instance()

    private val  mViewModel : AccountViewModel by lazy { ViewModelProviders.of(this,viewModelFactory).get(AccountViewModel::class.java) }


    override fun setLayoutId(): Int = R.layout.activity_register


    override fun initView() {
        super.initView()
        mBtnRegister.setOnClickListener {
            mViewModel.getRegister(mTieAccount.toString(), mRegPassword.toString(), mRegPassword.toString())
        }

        dataObserver()
    }


    fun dataObserver() {
        mViewModel.mRegisterData.observe(this, Observer { it ->

            it?.data?.let {
                toast(getString(R.string.register_suc))
                UserContext.instance.loginSuccess(it.username)
                finish()
            }
        })
    }

    override fun onBackPressed() = finish()
}
