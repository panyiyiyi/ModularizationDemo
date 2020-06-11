package com.even.chart_view.dialog

import android.graphics.Color
import android.view.View
import android.widget.LinearLayout
import com.even.chart_view.R
import com.even.chart_view.dialog.adapter.DataWheelAdapter
import com.even.chart_view.dialog.base.BaseDialogFragment
import com.even.chart_view.dialog.views.WheelView

/**
 *  @author  Created by Even on 2020/6/9
 *  Email: emailtopan@163.com
 *  数据选择弹窗
 */
class DataSelectorDialog : BaseDialogFragment {
    private lateinit var linearLayout: LinearLayout

    private var mVisibleItemCount = 3
    private var mTitle = ""
    private var mDataLists: MutableList<List<String>> = mutableListOf()
    private var mWheelViewLists = mutableListOf<WheelView>()

    constructor(visibleItemCount: Int, title: String, vararg lists: List<String>) : super() {
        this.mVisibleItemCount = visibleItemCount
        this.mTitle = title
        lists.forEach {
            mDataLists.add(it)
        }


    }

    override fun initView(view: View) {
        linearLayout = view.findViewById(R.id.llRoot)
        val initTitleBar = initTitleBar(mTitle)
        initTitleBar?.rightText?.setOnClickListener {


        }

        initWheel()
    }

    private fun initWheel() {
        mDataLists.forEachIndexed { index, list ->
            val wheelView = WheelView(context!!)
            val wheelAdapter = DataWheelAdapter(list, 0, 24f, 14f)
            wheelView.setVisibleItems(mVisibleItemCount)
            wheelView.setViewAdapter(wheelAdapter)
            wheelView.setCurrentItem(0)
            wheelView.tag = index
            wheelView.setBackgroundColor(Color.GREEN)

            mWheelViewLists.add(wheelView)

            val lp = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1f
            )
            linearLayout.addView(wheelView, lp)
        }
    }


    override fun getContentView(): Int = R.layout.dialog_data_select

}