package com.even.chart_view.dialog.imp

import com.even.chart_view.dialog.views.WheelView


/**
 *  @author  Created by Even on 2020/6/9
 *  Email: emailtopan@163.com
 *  wheel改变监听
 */
interface WheelChangedListener {
    /**
     * 当前Item改变回调
     */
    fun onChanged(wheel: WheelView, oldItem: Int, newInt: Int)

}