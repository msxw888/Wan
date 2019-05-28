package com.example.wan.UI.Knowledgesys

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
class KnowViewModelFactory (private val repository: NetworkDataimpl) : ViewModelProvider.NewInstanceFactory(){

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return KnowledgeViewModel(repository) as T
    }
}