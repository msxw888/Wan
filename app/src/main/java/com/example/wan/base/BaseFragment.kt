package com.example.wan.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("onViewCreated",javaClass.simpleName)
        initView()
    }

    abstract fun initView()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("onCreated",javaClass.simpleName)
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