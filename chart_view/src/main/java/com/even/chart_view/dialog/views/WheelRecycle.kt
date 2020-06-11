package com.even.chart_view.dialog.views

import android.view.View
import android.widget.LinearLayout
import java.util.*

/**
 *  @author  Created by Even on 2020/6/6
 *  Email: emailtopan@163.com
 *  wheel回收
 */
class WheelRecycle(private var mWheelView: WheelView) {
    /**
     * 缓存的Items
     */
    private var mItems: MutableList<View> = LinkedList<View>()

    /**
     * 缓存的空Item
     */
    private var mEmptyItems: MutableList<View> = LinkedList<View>()


    /**
     *  回收Item，将不再显示范围内的item放在缓存中
     *  @return 返回第一个item显示的位置
     */
    fun recycleItems(layout: LinearLayout, firstItem: Int, range: ItemsRange): Int {
        var index = firstItem
        var firstItem = firstItem
        var i = 0
        while (i < layout.childCount) {
            if (!range.contains(index)) {
                addCacheView(layout.getChildAt(i), index)
                layout.removeViewAt(i)
                if (i == 0)
                    firstItem++
            } else {
                i++
            }
            index++
        }
        return firstItem
    }

    /**
     * 获取缓存的第一个View
     */
    fun getItem(): View? {
        return getCachedView(mItems)
    }

    /**
     * 获取缓存的第一个空的ItemView
     */
    fun getEmptyItem(): View? {
        return getCachedView(mEmptyItems)
    }

    /**
     * 清除所有View
     */
    fun clearAll() {
        mItems.clear()
        mEmptyItems.clear()
    }

    /**
     * 获取缓存的第一个View
     * @return view 缓存的第一个View
     */
    private fun getCachedView(cache: MutableList<View>): View? {
        return if (!cache.isNullOrEmpty()) {
            val view = cache[0]
            cache.remove(view)
            view
        } else {
            null
        }
    }

    /**
     *  将View添加到缓存中 ，如果View在wheelView item区间内，则添加到item缓存中，如果不存在则添加倒emptyItem缓存中
     *  @param view 需要添加到缓存中的View
     *  @param index 需要添加View的下标
     */
    private fun addCacheView(view: View, index: Int) {
        val count = mWheelView.getViewAdapter()!!.getItemsCount()
        if (index !in 0 until count && !mWheelView.isCyclic()) {
            //不再区间内，并且没有被回收
            mEmptyItems.add(view)
        } else {
            mItems.add(view)
        }
    }
}