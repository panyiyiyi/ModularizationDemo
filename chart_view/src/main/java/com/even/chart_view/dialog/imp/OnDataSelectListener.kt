package com.even.chart_view.dialog.imp

/**
 *  @author  Created by Even on 2020/6/11
 *  Email: emailtopan@163.com
 *  数据选择回调
 */
interface OnDataSelectListener {
    /**
     * 选择数据回调
     * @param resultList 选择数据集合
     * @param resultIndexList 选择数据下标回调
     */
    fun onDataSelect(resultList: List<String>, resultIndexList: List<Int>)
}