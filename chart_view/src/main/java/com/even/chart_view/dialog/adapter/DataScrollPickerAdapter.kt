package com.even.chart_view.dialog.adapter

import com.even.chart_view.R
import com.even.commonrv.adapter.BaseViewHolder

/**
 *  @author  Created by Even on 2020/6/19
 *  Email: emailtopan@163.com
 *
 */
class DataScrollPickerAdapter : AbstractScrollPickerAdapter {
    constructor(dataList: MutableList<String>) : super(dataList, R.layout.view_item_wheel_data)

    override fun covert(holder: BaseViewHolder, item: String, position: Int) {
        holder.setText(R.id.tvContent, item)

    }

    override fun getVisibleItemNum(): Int = 5
    override fun isShowLines(): Boolean = false
}