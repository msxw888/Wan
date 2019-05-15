package com.example.wan.UI.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wan.repository.remote.NetworkDataimpl

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/3/30
 * @Version：
 *
 */
class MainViewModelFactory (private val repository: NetworkDataimpl) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}