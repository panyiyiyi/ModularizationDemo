package com.even.chart_view.dialog.adapter

import android.graphics.Color
import android.util.TypedValue
import android.view.ViewGroup
import android.widget.TextView
import com.even.chart_view.R

/**
 *  @author  Created by Even on 2020/6/11
 *  Email: emailtopan@163.com
 *
 */
class DataWheelAdapter : AbstractWheelTextAdapter {
    private var maxSize: Float
    private var minSize: Float
    private var dataList: List<String>

    constructor(
        lists: List<String>,
        currentItem: Int,
        maxSize: Float,
        minSize: Float
    ) : super(R.layout.view_item_wheel_data, R.id.tvContent, currentItem, maxSize, minSize) {
        this.dataList = lists
        this.maxSize = maxSize
        this.minSize = minSize
    }

    override fun getItemText(index: Int): CharSequence? = dataList[index]

    override fun getItemsCount(): Int = dataList.size

    override fun moveChange(itemsLayout: ViewGroup, currentItem: Int) {
        for (i in 0 until itemsLayout.childCount) {
            val view = itemsLayout.getChildAt(i)
            val centerTextView = when {
                view is TextView -> view
                itemTextResourceId > 0 -> view.findViewById(itemTextResourceId)
                else -> null
            }
            if (centerTextView != null) {
                centerTextView.setTextSize(
                    TypedValue.COMPLEX_UNIT_DIP,
                    if (view.tag == currentItem) maxSize else minSize
                )
                centerTextView.setTextColor(
                    if (view.tag == currentItem) Color.parseColor("#26b7bc") else Color.parseColor(
                        "#333333"
                    )
                )
            }
        }

    }
}