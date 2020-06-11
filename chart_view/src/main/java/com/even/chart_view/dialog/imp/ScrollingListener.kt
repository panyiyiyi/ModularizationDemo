package com.even.chart_view.dialog.imp

/**
 *  @author  Created by Even on 2020/6/8
 *  Email: emailtopan@163.com
 *  Wheel滑动监听
 */
interface ScrollingListener {

    /**
     * 发生滚动的时候回调
     */
    fun onScroll(distance: Int)

    /**
     * 滚动开始回调
     */
    fun onStarted()

    /**
     * 滑动结束之后调用该回调
     */
    fun onJustify()

    /**
     *  在执行完onJustify之后调用该回调
     */
    fun onFinished()

}