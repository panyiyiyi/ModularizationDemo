package com.even.chart_view.dialog.imp

import com.even.chart_view.dialog.views.WheelView


/**
 *  @author  Created by Even on 2020/6/8
 *  Email: emailtopan@163.com
 *  wheel滑动监听
 */
interface WheelScrollListener {
    /**
     * 开始滑动的时候调用
     */
    fun onScrollingStarted(wheelView: WheelView)

    /**
     * 滑动结束的时候调用
     */
    fun onScrollingFinish(wheelView: WheelView)
}