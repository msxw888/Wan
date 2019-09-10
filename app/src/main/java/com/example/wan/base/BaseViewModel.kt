package com.example.wan.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * 文件描述：
 * @author：WJH
 * @Creatdate：2019/9/11
 * @Version：
 *
 */
open class BaseViewModel : ViewModel() {
    val netstate = MutableLiveData<Boolean>()

}