package com.even.chartdemo

import android.view.View
import com.even.chart_view.bean.DialogTitleBarBean
import com.even.chart_view.dialog.base.BaseDialogFragment

/**
 *  @author  Created by Even on 2020/4/13
 *  Email: emailtopan@163.com
 *  悬着弹窗
 */
class SelectDialogFragment : BaseDialogFragment() {
    override fun initView(view: View) {
        initTitleBar("biaoti")
    }


    override fun getContentView(): Int = R.layout.activity_text

    override fun isCancelDialogOnOutSize(): Boolean = false


}