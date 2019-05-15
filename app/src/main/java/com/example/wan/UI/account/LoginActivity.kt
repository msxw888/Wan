package com.example.wan.UI.account

import android.app.Activity
import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.wan.R
import com.example.wan.base.BaseActivity
import com.example.wan.toast
import kotlinx.android.synthetic.main.activity_login.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class LoginActivity : BaseActivity() ,View.OnClickListener,KodeinAware{
    override val kodein by closestKodein()
    private val viewModelFactory: AccountViewModelFactory by instance()

    private lateinit var mViewModel : AccountViewModel


    override fun setLayoutId(): Int = R.layout.activity_login

    override fun initView() {
        super.initView()

        mViewModel = ViewModelProviders.of(this,viewModelFactory).get(AccountViewModel::class.java)

        mBtnLogin.setOnClickListener(this)
        mTvRegister.setOnClickListener(this)
        // 处理 repository 回调的数据
        mViewModel.mLoginData.observe(this, Observer {
            it?.let { loginRsp ->
//                UserContext.instance.loginSuccess(loginRsp.username, loginRsp.collectIds)
                setResult(
                    Activity.RESULT_OK,
                    Intent().apply { putExtra(Constant.CONTENT_TITLE_KEY, loginRsp.usern) })
                toast(getString(R.string.login_suc))
                finish()
            }
        })
        showSuccess()
    }

    private fun showSuccess() {

    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.mBtnLogin ->{
                mViewModel.getLoginData(mTieAccount.text.toString(), mTiePassword.text.toString())
            }
//            R.id.mTvRegister ->{
//                startActivity<RegisterActivity>()
//                finish()
//            }
        }
    }
}
