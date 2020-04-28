package com.even.common_utils

import android.app.Activity
import android.content.res.Resources

/**
 *  @author  Created by Even on 2020/4/27
 *  Email: emailtopan@163.com
 *
 */
object DisplayUtils {
    /**
     * dp转换成px
     * @param dipValue  dp值
     */
    fun dip2px(dipValue: Int): Float {
        val density = Resources.getSystem().displayMetrics.density
        return (dipValue * density + 0.5f)
    }

    /**
     * px转换成dp
     * @param pxValue 像素值
     */
    fun px2dip(pxValue: Int): Float {
        val density = Resources.getSystem().displayMetrics.density
        return (pxValue / density + 0.5f)
    }

    /**
     * sp转换成px
     * @param spValue sp值
     */
    fun sp2px(spValue: Int): Float {
        val scale = Resources.getSystem().displayMetrics.scaledDensity
        return (spValue * scale + 0.5f)
    }

    /**
     * px转换成sp
     * @param pxValue 像素值
     */
    fun px2sp(pxValue: Int): Float {
        val scale = Resources.getSystem().displayMetrics.scaledDensity
        return (pxValue / scale + 0.5f)
    }

    /**
     * 获取屏幕高度
     * @return 屏幕像素高度
     */
    fun getScreenHeight(): Int {
        val displayMetrics = Resources.getSystem().displayMetrics
        return displayMetrics.heightPixels
    }

    /**
     * 获取屏幕宽度
     * @return 屏幕像素宽度
     */
    fun getScreenWidth(): Int {
        val displayMetrics = Resources.getSystem().displayMetrics
        return displayMetrics.widthPixels
    }

    /**
     * 获取状态栏高度
     * @return 返回状态栏像素值
     */
    fun getStatusBarHeight(activity: Activity): Int {
        val identifier = activity.resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (identifier > 0) activity.resources.getDimensionPixelSize(identifier) else 60
    }
}