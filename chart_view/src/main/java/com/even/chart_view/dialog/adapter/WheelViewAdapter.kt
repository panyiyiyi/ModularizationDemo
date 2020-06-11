package com.even.chart_view.dialog.adapter

import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent

/**
 *  @author  Created by Even on 2020/6/6
 *  Email: emailtopan@163.com
 *  wheel items adapter interface
 */
interface WheelViewAdapter {
    /**
     * 获取item数量
     */
    fun getItemsCount(): Int

    /**
     *  获取指定index的View
     */
    fun getItem(index: Int, currentItem: Int, convertView: View?, parent: ViewGroup): View?

    /**
     * 获取显示第一个Item前一个，或者最后一个后一个的空Item的View ，复用
     */
    fun getEmptyItem(convertView: View?, parent: ViewGroup): View?

    /**
     * 注册观察者，用来观察adapter的改变情况
     */
    fun registerDataSetObserver(observer: DataSetObserver)

    /**
     * 反注册观察者
     */
    fun unregisterDataSetObserver(observer: DataSetObserver)

    /**
     * 移动到指定的item
     */
    fun moveChange(itemsLayout: ViewGroup, currentItem: Int)
}