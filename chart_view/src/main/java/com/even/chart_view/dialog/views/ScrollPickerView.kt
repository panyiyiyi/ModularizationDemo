package com.even.chart_view.dialog.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.even.chart_view.dialog.imp.IScrollPickerOperation
import com.even.common_utils.DisplayUtils

/**
 *  @author  Created by Even on 2020/6/19
 *  Email: emailtopan@163.com
 *
 */
class ScrollPickerView : RecyclerView {
    private lateinit var mPaint: Paint
    private lateinit var mSmoothScrollTask: Runnable

    //Item高度
    private var mItemHeight = 0

    //Item 宽度
    private var mItemWidth = 0

    //第一条线位置
    private var mFirstLineY = 0f

    //第二条线位置
    private var mSecondLineY = 0f

    private var mInitialY = 0

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
        initTask()
    }

    private fun init() {
        mPaint = Paint()
        mPaint.color = getLineColor()
        mPaint.strokeWidth = DisplayUtils.dip2px(1)
    }

    /**
     * 绘制中间间隔线
     */
    private fun drawLines(canvas: Canvas) {
        if (mItemHeight > 0 && isShowLines()) {
            //有数据
            canvas.drawLine(0f, mFirstLineY, width.toFloat(), mFirstLineY, mPaint)
            canvas.drawLine(0f, mSecondLineY, width.toFloat(), mSecondLineY, mPaint)
        }
    }

    private fun getScrollYDistance(): Int {
        val linearLayoutManager = layoutManager as LinearLayoutManager ?: return 0
        val firstPosition = linearLayoutManager.findFirstVisibleItemPosition()
        val firstView = linearLayoutManager.findViewByPosition(firstPosition) ?: return 0
        return firstView.height * firstPosition - firstView.top
    }

    /**
     * 获取可见Item数量
     */
    private fun getVisibleNum(): Int {
        val operation = adapter as IScrollPickerOperation
        return operation.getVisibleItemNum()
    }

    /**
     * 获取分割线颜色
     */
    private fun getLineColor(): Int {
        if (adapter != null) {
            val operation = adapter as IScrollPickerOperation
            return operation.getLineColor()
        }
        return Color.GRAY
    }

    /**
     * 是否显示中间线
     */
    private fun isShowLines(): Boolean {
        val operation = adapter as IScrollPickerOperation
        return operation.isShowLines()
    }


    private fun processItemOffset() {
        //手指抬起
        mInitialY = getScrollYDistance()
        postDelayed(mSmoothScrollTask, 30)

    }

    private fun initTask() {
        mSmoothScrollTask = Runnable {
            val newY = getScrollYDistance()
            if (mInitialY != newY) {
                mInitialY = getScrollYDistance()
                postDelayed(mSmoothScrollTask, 30)
            } else if (mItemHeight > 0) {
                val offset: Int = mInitialY % mItemHeight + paddingTop //离选中区域中心的偏移量
                if (offset == 0) {
                    return@Runnable
                }
                if (offset >= mItemHeight / 2) { //滚动区域超过了item高度的1/2，调整position的值
                    smoothScrollBy(0, mItemHeight - offset)
                } else if (offset < mItemHeight / 2) {
                    smoothScrollBy(0, -offset)
                }
            }
        }
    }


    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (e.action == MotionEvent.ACTION_UP) {
            processItemOffset()
        }
        return super.onTouchEvent(e)
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        if (childCount > 0) {
            mItemHeight = getChildAt(0).measuredHeight
            mItemWidth = getChildAt(0).measuredWidth
        }
        setMeasuredDimension(mItemWidth, mItemHeight * getVisibleNum() + paddingBottom + paddingTop)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mFirstLineY = (mItemHeight * (getVisibleNum() - 1) / 2 + paddingTop).toFloat()
        mSecondLineY = (mItemHeight * (getVisibleNum() + 1) / 2 + paddingTop).toFloat()

    }

    override fun onDraw(c: Canvas) {
        super.onDraw(c)
        drawLines(c)
    }
}