package com.even.chart_view.dialog.imp

/**
 *  @author  Created by Even on 2020/6/19
 *  Email: emailtopan@163.com
 *  选择器操作接口
 */
interface IScrollPickerOperation {
    /**
     * 获取看见Item数量
     */
    fun getVisibleItemNum(): Int

    /**
     * 中间分割线颜色
     */
    fun getLineColor(): Int

    /**
     * 是否显示中间线
     */
    fun isShowLines(): Boolean
}