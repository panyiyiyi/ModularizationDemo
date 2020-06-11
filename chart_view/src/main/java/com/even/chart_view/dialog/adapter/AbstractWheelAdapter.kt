package com.even.chart_view.dialog.adapter

import android.database.DataSetObserver
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent

/**
 *  @author  Created by Even on 2020/6/10
 *  Email: emailtopan@163.com
 *  抽象Wheel adapter
 */
abstract class AbstractWheelAdapter : WheelViewAdapter {
    private var mDataSetObservers = mutableSetOf<DataSetObserver>()

    override fun getEmptyItem(convertView: View?, parent: ViewGroup): View? = null

    override fun registerDataSetObserver(observer: DataSetObserver) {
        mDataSetObservers.add(observer)
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver) {
        mDataSetObservers.remove(observer)
    }

    /**
     * 数据改变通知
     */
    fun notifyDataChangedEvent() {
        mDataSetObservers.forEach {
            it.onChanged()
        }
    }

    /**
     * 重绘通知
     */
    fun notifyDataInvalidatedEvent() {
        mDataSetObservers.forEach { it.onInvalidated() }
    }

}