package com.even.chart_view.dialog.imp

import com.even.chart_view.dialog.views.WheelView


/**
 *  @author  Created by Even on 2020/6/9
 *  Email: emailtopan@163.com
 *  item 点击回调
 */
interface WheelClickedListener {
    /**
     * wheelView  item 点击回调
     */
    fun onItemClick(wheelView: WheelView, itemIndex: Int)

}