package com.even.chart_view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.even.common_utils.DisplayUtils
import com.even.common_utils.PaintUtils

/**
 *  @author  Created by Even on 2020/4/30
 *  Email: emailtopan@163.com
 *  比例进度条
 */
class RateProgressBarView : View {
    /**
     * 进度条长度
     */
    private var mWidth = 0f
    /**
     * progress起始坐标
     */
    private var mStartX = 0f
    private var mStartY = 0f
    /**
     * progress分界点
     */
    private var mFirstDot = 0f
    private var mSecondDot = 0f
    /**
     * 进度条高度,默认颜色
     */
    private var mProgressBarHeight: Float
    private var mProgressBarColor: Int

    /**
     * 进度条画笔
     */
    private lateinit var mPaint: Paint
    /**
     * 文本画笔
     */
    private lateinit var mTextPaint: Paint
    /**
     * 圆环画笔
     */
    private lateinit var mCirclePaint: Paint
    /**
     * 第一点、第二点的值
     */
    private var mFirstDotValue: Float
    private var mSecondDotValue: Float
    /**
     * 当前的值
     */
    private var mCurrentValue: Float
    /**
     * 分界点文本字体大小
     */
    private var mDotTextSize: Float
    /**
     * 分界点文本字体颜色
     */
    private var mDotTextColor: Int
    /**
     * 当前值字体大小
     */
    private var mCurrentValueSize: Float

    /**
     * 进度三种等级的颜色
     */
    private var mLowColor: Int
    private var mNormalColor: Int
    private var mOverColor: Int


    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typeArray =
            context.obtainStyledAttributes(attrs, R.styleable.RateProgressBarView)
        mProgressBarHeight = typeArray.getDimension(
            R.styleable.RateProgressBarView_progressHeight,
            DisplayUtils.dip2px(10)
        )
        mProgressBarColor =
            typeArray.getColor(R.styleable.RateProgressBarView_progressDefaultColor, Color.GRAY)
        mFirstDotValue = typeArray.getFloat(R.styleable.RateProgressBarView_firstDotValue, 0f)
        mSecondDotValue = typeArray.getFloat(R.styleable.RateProgressBarView_secondDotValue, 0f)
        mCurrentValue = typeArray.getFloat(R.styleable.RateProgressBarView_currentValue, 0f)

        mDotTextSize = typeArray.getDimension(
            R.styleable.RateProgressBarView_dotTextSize,
            DisplayUtils.sp2px(14)
        )
        mCurrentValueSize = typeArray.getDimension(
            R.styleable.RateProgressBarView_currentValueTextSize,
            DisplayUtils.sp2px(16)
        )
        mDotTextColor = typeArray.getColor(R.styleable.RateProgressBarView_dotTextColor, Color.GRAY)

        mLowColor = typeArray.getColor(R.styleable.RateProgressBarView_lowColor, Color.GRAY)
        mNormalColor = typeArray.getColor(R.styleable.RateProgressBarView_normalColor, Color.GRAY)
        mOverColor = typeArray.getColor(R.styleable.RateProgressBarView_overColor, Color.GRAY)

        typeArray.recycle()
        init()


    }

    /**
     * 设值
     * @param firstDotValue  第一个点值
     * @param secondDotValue  第二点值
     * @param currentValue  当前值
     */
    fun setValue(firstDotValue: Float, secondDotValue: Float, currentValue: Float) {
        this.mFirstDotValue = firstDotValue
        this.mSecondDotValue = secondDotValue
        this.mCurrentValue = currentValue
        invalidate()
    }

    /**
     * 设值区间颜色
     * @param lowColor  少了颜色
     * @param normalColor  正常颜色
     * @param overColor  超过颜色
     */
    fun setValueColor(lowColor: Int, normalColor: Int, overColor: Int) {
        this.mLowColor = lowColor
        this.mNormalColor = normalColor
        this.mOverColor = overColor
        invalidate()
    }

    private fun init() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.strokeWidth = mProgressBarHeight
        mPaint.color = mProgressBarColor
        mPaint.strokeCap = Paint.Cap.ROUND

        mTextPaint = Paint()
        mTextPaint.isAntiAlias = true
        mTextPaint.textAlign = Paint.Align.CENTER


        mCirclePaint = Paint()
        mCirclePaint.isAntiAlias = true
        mCirclePaint.color = Color.WHITE

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val height = if (heightMode == MeasureSpec.AT_MOST) {
            widthSize / 3
        } else {
            heightSize
        }
        setMeasuredDimension(widthSize, height)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        mWidth = (measuredWidth - paddingStart - paddingEnd).toFloat()
        mFirstDot = mWidth / 4 + paddingStart
        mSecondDot = mWidth * 3 / 4 + paddingStart

        mStartX = paddingStart.toFloat()
        mStartY = (measuredHeight / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        mPaint.color = mProgressBarColor
        mPaint.strokeWidth = mProgressBarHeight
        mPaint.strokeCap = Paint.Cap.ROUND
        //绘制默认线
        canvas.drawLine(
            mStartX,
            mStartY,
            mWidth + mStartX,
            mStartY,
            mPaint
        )
        drawValueView(canvas)
        drawNormalView(canvas)

    }

    /**
     * 绘制数据View
     */
    private fun drawValueView(canvas: Canvas) {
        mPaint.strokeWidth = mProgressBarHeight
        mPaint.strokeCap = Paint.Cap.ROUND
        val currentProgress = when {
            mCurrentValue < mFirstDotValue -> {
                //少了
                mPaint.color = mLowColor
                mCirclePaint.color = mLowColor
                mTextPaint.color = mLowColor
                val fl =
                    mStartX + mWidth / 4 * mCurrentValue / mFirstDotValue
                fl
            }
            mCurrentValue > mSecondDotValue -> {
                //多了
                mPaint.color = mOverColor
                mCirclePaint.color = mOverColor
                mTextPaint.color = mOverColor
                val fl =
                    mSecondDot + mWidth / 4 * (mCurrentValue - mSecondDotValue) / mCurrentValue
                fl
            }
            else -> {
                //刚刚好
                mPaint.color = mNormalColor
                mCirclePaint.color = mNormalColor
                mTextPaint.color = mNormalColor
                val fl =
                    mFirstDot + ((mCurrentValue - mFirstDotValue) * mWidth) / ((mSecondDotValue - mFirstDotValue) * 2)
                fl
            }
        }
        //当前值的线
        canvas.drawLine(mStartX, mStartY, currentProgress, mStartY, mPaint)
        //当前值的Value

        mTextPaint.textSize = mCurrentValueSize
        canvas.drawText(
            mCurrentValue.toString(),
            currentProgress,
            mStartY - PaintUtils.getFontHeight(mTextPaint),
            mTextPaint
        )

        canvas.drawCircle(currentProgress, mStartY, mProgressBarHeight * 2 / 3, mCirclePaint)
        mCirclePaint.color = Color.WHITE
        canvas.drawCircle(currentProgress, mStartY, mProgressBarHeight / 2, mCirclePaint)


    }


    /**
     * 绘制基础View
     */
    private fun drawNormalView(canvas: Canvas) {
        //绘制分界线
        mPaint.color = Color.WHITE
        mPaint.strokeCap = Paint.Cap.BUTT
        mPaint.strokeWidth = mProgressBarHeight / 2
        canvas.drawLine(
            mFirstDot,
            mStartY + mProgressBarHeight / 4,
            mFirstDot + 3,
            mStartY + mProgressBarHeight / 4,
            mPaint
        )
        canvas.drawLine(
            mSecondDot,
            mStartY + mProgressBarHeight / 4,
            mSecondDot + 3,
            mStartY + mProgressBarHeight / 4,
            mPaint
        )
        //绘制第一，第二点文本

        mTextPaint.textSize = mDotTextSize
        mTextPaint.color = mDotTextColor
        canvas.drawText(
            mFirstDotValue.toString(),
            mFirstDot,
            mStartY + mProgressBarHeight + PaintUtils.getFontHeight(mTextPaint),
            mTextPaint
        )
        canvas.drawText(
            mSecondDotValue.toString(),
            mSecondDot,
            mStartY + mProgressBarHeight + PaintUtils.getFontHeight(mTextPaint),
            mTextPaint
        )
    }
}