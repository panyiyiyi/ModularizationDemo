package com.even.chart_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.even.chart_view.bean.CircularRateBean
import com.even.common_utils.DisplayUtils
import com.even.common_utils.PaintUtils

/**
 *  @author  Created by Even on 2020/4/27
 *  Email: emailtopan@163.com
 *  圆环比例自定义View
 */
class CircularRateView : View {
    /**
     * 绘制View的Paint
     */
    private lateinit var mPaint: Paint
    /**
     * 绘制文字的Paint
     */
    private lateinit var mTextPaint: Paint
    /**
     * 圆环内切圆矩形
     */
    private lateinit var mRectF: RectF
    /**
     * 主文本字体大小，颜色
     */
    private var mFirstTextSize: Float
    private var mFirstTextColor: Int
    /**
     * 副文本字体大小，颜色
     */
    private var mSecondTextSize: Float
    private var mSecondTextColor: Int
    /**
     * 圆环中心点坐标
     */
    private var mCenterX = 0f
    private var mCenterY = 0f
    /**
     * 圆环半径
     */
    private var mRadius = 0f
    /**
     * 默认圆环颜色
     */
    private var mDefaultRingColor: Int
    /**
     * 圆环宽度
     */
    private var mRingWidth: Float
    /**
     * 总额
     */
    private var mAccount = 0f
    /**
     * 圆环数据集合
     */
    private var mRingDataLists = mutableListOf<CircularRateBean>()
    /**
     * 提示文本，可以外部设值
     */
    var remindStr: String?

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    @SuppressLint("Recycle")
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CircularRateView)
        mDefaultRingColor =
            typeArray.getColor(R.styleable.CircularRateView_defaultRingColor, Color.GRAY)
        mRingWidth =
            typeArray.getDimension(R.styleable.CircularRateView_ringWidth, DisplayUtils.dip2px(10))
        mFirstTextSize = typeArray.getDimension(
            R.styleable.CircularRateView_firstTextSize,
            DisplayUtils.sp2px(16)
        )
        mFirstTextColor =
            typeArray.getColor(R.styleable.CircularRateView_firstTextColor, Color.BLACK)
        mSecondTextSize = typeArray.getDimension(
            R.styleable.CircularRateView_secondTextSize,
            DisplayUtils.sp2px(14)
        )
        mSecondTextColor =
            typeArray.getColor(R.styleable.CircularRateView_secondTextColor, Color.GRAY)
        remindStr = typeArray.getString(R.styleable.CircularRateView_remindText)

        typeArray.recycle()
        init()

    }

    /**
     * 设值
     * @param dataLists 圆环比例集合
     */
    fun setRateValue(dataLists: MutableList<CircularRateBean>) {
        this.mRingDataLists = dataLists
        mAccount = 0f
        dataLists.forEach {
            mAccount += it.ringValue
        }
        invalidate()
    }

    /**
     * 初始化
     */
    private fun init() {
        mPaint = Paint()
        mPaint.isAntiAlias = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mRingWidth
        mPaint.color = mDefaultRingColor

        mTextPaint = Paint()
        mTextPaint.isAntiAlias = true
        mTextPaint.textSize = mFirstTextSize
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.color = mFirstTextColor

        mRectF = RectF()
    }

    /**
     * 计算宽高
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)

        val height = if (heightMode == MeasureSpec.AT_MOST) {
            //设置为可以最大尺寸的时候，设置高度为宽度的2/3
            widthSize / 3 * 2 + paddingBottom + paddingTop
        } else {
            heightSize
        }
        setMeasuredDimension(widthSize, height)
    }

    /**
     * 确定坐标
     */
    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        mCenterX = (measuredWidth / 2).toFloat()
        mCenterY = (measuredHeight / 2).toFloat()
        mRadius = (measuredWidth / 4).toFloat()

        mRectF.set(
            mCenterX - mRadius - mRingWidth / 2,
            mCenterY - mRadius - mRingWidth / 2,
            mCenterX + mRadius + mRingWidth / 2,
            mCenterY + mRadius + mRingWidth / 2
        )
    }

    /**
     * 绘制
     */
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //默认圆环
        canvas.drawArc(mRectF, 0f, 360f, false, mPaint)
        //当前角度
        var currentAngle = -90f

        mRingDataLists.forEach {
            val angle = it.ringValue / mAccount * 360
            mPaint.color = it.ringColor
            canvas.drawArc(mRectF, currentAngle, angle, false, mPaint)
            currentAngle += angle
        }

        if (remindStr.isNullOrEmpty()) {  //不存在副文本
            //绘制主文本，处于正中间
            canvas.drawText(
                mAccount.toString(),
                mCenterX,
                mCenterY + PaintUtils.getFontHeight(mTextPaint) / 2,
                mTextPaint
            )
        } else {
            //绘制主文本，位置是文字底部的位置
            canvas.drawText(mAccount.toString(), mCenterX, mCenterY - 5f, mTextPaint)

            //绘制副文本
            mTextPaint.color = mSecondTextColor
            mTextPaint.textSize = mSecondTextSize
            canvas.drawText(
                remindStr!!,
                mCenterX,
                mCenterY + PaintUtils.getFontHeight(mTextPaint) + 5f,
                mTextPaint
            )
        }
    }


}

