package com.example.wan.base

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavDestination
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.example.wan.R
import java.util.*

/**
 * 文件描述：
 * @author：WJH
 * @Creatdata：2019/6/8
 * @Version：
 *
 */
@Navigator.Name("MyNav") // `keep_state_fragment`用于导航xml
class MyNav(
    private val context: Context,
    private val manager: FragmentManager,
    private val containerId: Int
    ) : FragmentNavigator(context, manager, containerId) {

    private val mBackStack = ArrayDeque<Int>()

    private val TAG = "FragmentNavigator"


    override fun navigate(
        destination: Destination,
        args: Bundle?,
        navOptions: NavOptions?,
        navigatorExtras: Navigator.Extras?
    ): NavDestination? {

        if (manager.isStateSaved) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already" + " saved its state")
            return null
        }
        var className = destination.className
        if (className[0] == '.') {
            className = context.packageName + className
        }
        /**
         * 原创建frag
         */
//        val frag = instantiateFragment(
//            context, manager,
//            className, args
//        )
//        frag.arguments = args


        val ft = manager.beginTransaction()

        var enterAnim = navOptions?.enterAnim ?: -1
        var exitAnim = navOptions?.exitAnim ?: -1
        var popEnterAnim = navOptions?.popEnterAnim ?: -1
        var popExitAnim = navOptions?.popExitAnim ?: -1
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = if (enterAnim != -1) enterAnim else 0
            exitAnim = if (exitAnim != -1) exitAnim else 0
            popEnterAnim = if (popEnterAnim != -1) popEnterAnim else 0
            popExitAnim = if (popExitAnim != -1) popExitAnim else 0
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim)
        }
        //原切换方式
//        ft.replace(mContainerId, frag)

        //新切换
        val tag = destination.id.toString()
        var initialNavigate = false
        val currentFragment = manager.primaryNavigationFragment
        if (currentFragment != null) {
            ft.hide(currentFragment)
        } else {
            initialNavigate = true
        }

        var fragment = manager.findFragmentByTag(tag)
        if (fragment == null) {
            val className = destination.className
            fragment = manager.fragmentFactory.instantiate(context.classLoader, className)
            ft.add(containerId, fragment, tag)
        } else {
            ft.show(fragment)
        }


        ft.setPrimaryNavigationFragment(fragment)

        @IdRes val destId = destination.id
        val initialNavigation = mBackStack.isEmpty()
        // TODO 为片段构建第一类singleTop行为
        val isSingleTopReplacement = (navOptions != null && !initialNavigation
                && navOptions.shouldLaunchSingleTop()
                && mBackStack.peekLast() == destId)

        val isAdded: Boolean
        if (initialNavigation) {
            isAdded = true
        } else if (isSingleTopReplacement) {
            // Single Top意味着我们只想在后台堆栈上有一个实例
            if (mBackStack.size > 1) {
                //如果要替换的片段在FragmentManager上
                //返回堆栈，简单的replace（）是不够的，所以我们
                //从后面的堆栈中删除它并放置我们的替换
                //在后面的堆栈上
                manager.popBackStack(
                    generateBackStackName(mBackStack.size, mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                ft.addToBackStack(generateBackStackName(mBackStack.size, destId))
            }
            isAdded = false
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size + 1, destId))
            isAdded = true
        }
        if (navigatorExtras is Extras) {
            val extras = navigatorExtras as Extras?
            for ((key, value) in extras!!.sharedElements) {
                ft.addSharedElement(key, value)
            }
        }
        ft.setReorderingAllowed(true)
        ft.commit()
        // The commit succeeded, update our view of the world
        if (isAdded&&initialNavigate) {
            mBackStack.add(destId)
            return destination
        } else {
            return null
        }
    }

    private fun generateBackStackName(backStackIndex: Int, destId: Int): String {
        return "$backStackIndex-$destId"
    }
}