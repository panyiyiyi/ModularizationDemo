package com.even.common_utils

import android.graphics.Paint

/**
 *  @author  Created by Even on 2020/4/28
 *  Email: emailtopan@163.com
 *  画笔工具类
 */
object PaintUtils {
    /**
     * 返回指定画笔的字符串的长度
     * @param paint 画笔
     * @param str  指定字符串
     */
    fun getFontLength(paint: Paint, str: String) = paint.measureText(str)

    /**
     * 返回指定画笔离文字顶部的基准距离
     */
    fun getFontLeading(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return fontMetrics.leading - fontMetrics.ascent + 0.5f
    }

    /**
     * 返回指定画笔的文字高度
     */
    fun getFontHeight(paint: Paint): Float {
        val fontMetrics = paint.fontMetrics
        return fontMetrics.descent - fontMetrics.ascent * 0.5f
    }
}