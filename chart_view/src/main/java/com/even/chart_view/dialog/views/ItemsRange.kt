package com.even.chart_view.dialog.views

/**
 *  @author  Created by Even on 2020/6/6
 *  Email: emailtopan@163.com
 *  item可见范围
 */
open class ItemsRange {
    //默认创建显示样式都为0个
    private var mFirst: Int
    private var mCount: Int

    constructor() {
        mFirst = 0
        mCount = 0
    }

    constructor(mFirst: Int, mCount: Int) {
        this.mFirst = mFirst
        this.mCount = mCount
    }

    /**
     * 获取最后一个Item下标
     */
    open fun getLast(): Int = mFirst + mCount - 1

    /**
     * 获取第一个下标
     */
    open fun getFirst(): Int = mFirst

    /**
     * 获取可现实Item数量
     */
    open fun getCount(): Int = mCount

    /**
     * 判断index是否在可见区域
     */
    open fun contains(index: Int): Boolean = index >= getFirst() && index <= getLast()
}