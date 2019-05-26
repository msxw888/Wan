package com.example.wan.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {

//    protected lateinit var loadService: LoadService<*>


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initData()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Log.e("Fragment",javaClass.simpleName)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    open fun initData() {}

    // 重新加载
    open fun reLoad() {
        initData()
    }

}