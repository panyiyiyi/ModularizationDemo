package com.even.chart_view.dialog.adapter

import android.graphics.Color
import com.even.chart_view.dialog.imp.IScrollPickerOperation
import com.even.commonrv.adapter.BaseRecyclerAdapter

/**
 *  @author  Created by Even on 2020/6/19
 *  Email: emailtopan@163.com
 *  选择器抽象adapter
 */
abstract class AbstractScrollPickerAdapter : BaseRecyclerAdapter<String>,
    IScrollPickerOperation {
    constructor(dataLists: MutableList<String>, layoutId: Int) : super(dataLists, layoutId) {
        for (i in 0 until (getVisibleItemNum() - 1) / 2) {
            dataLists.add(0, "哈哈哈")
            dataLists.add("哈哈哈")
        }
    }

    override fun getVisibleItemNum(): Int = 3

    override fun getLineColor(): Int = Color.GRAY

    override fun isShowLines(): Boolean = true
}