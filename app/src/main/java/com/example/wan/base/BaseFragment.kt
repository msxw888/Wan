package com.example.wan.base

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onViewCreated",javaClass.simpleName)
        initView()

    }

    open fun networkError() {}

    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("onCreate",javaClass.simpleName)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        networkError()
        Log.d("onActivityCreated",javaClass.simpleName)
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("onDetach",javaClass.simpleName)
    }

    //    open fun initData() {}

    // 重新加载
//    open fun reLoad() {
//        initData()
//    }

}