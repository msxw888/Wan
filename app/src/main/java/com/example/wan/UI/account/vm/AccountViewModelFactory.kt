package com.example.wan.UI.account.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wan.UI.account.AccountRepository

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/3/30
 * @Version：
 *
 */
class AccountViewModelFactory (private val repository: AccountRepository) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AccountViewModel(repository) as T
    }
}