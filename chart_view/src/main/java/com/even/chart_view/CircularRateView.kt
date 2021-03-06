package com.even.chart_view

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
import kotlin.math.cos
import kotlin.math.sin

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
     * 圆点画笔
     */
    private lateinit var mCirclePaint: Paint

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
     * 文字与圆环距离
     */
    private var mDistance: Float

    /**
     * 提示文本大小
     */
    private var mRemindTextSize: Float

    /**
     * 提示文本颜色
     */
    private var mRemindTextColor: Int

    /**
     * 默认圆环颜色
     */
    private var mDefaultRingColor: Int

    /**
     * 圆环宽度
     */
    private var mRingWidth: Float

    /**
     * 折线颜色
     */
    private var mBrokenLineColor: Int

    /**
     * 圆点半径
     */
    private var mCircleRadius: Float

    /**
     * 圆点颜色
     */
    private var mCircleColor: Int

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
    private var mSecondText: String?

    /**
     * 是否显示提示文字
     */
    private var mIsShowRemindText: Boolean

    /**
     * 绘制折线坐标
     */
    private val mBrokenLineArray = FloatArray(8)

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
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
        mSecondText = typeArray.getString(R.styleable.CircularRateView_secondText)

        mRemindTextSize = typeArray.getDimension(
            R.styleable.CircularRateView_remindTextSize,
            DisplayUtils.sp2px(10)
        )
        mRemindTextColor =
            typeArray.getColor(R.styleable.CircularRateView_remindTextColor, Color.GRAY)

        mRadius = typeArray.getDimension(
            R.styleable.CircularRateView_ringRadius,
            DisplayUtils.dip2px(50)
        )

        mBrokenLineColor =
            typeArray.getColor(R.styleable.CircularRateView_brokenLineColor, Color.GRAY)

        mCircleColor = typeArray.getColor(R.styleable.CircularRateView_circleColor, Color.GRAY)

        mCircleRadius = typeArray.getDimension(
            R.styleable.CircularRateView_circleRadius,
            DisplayUtils.dip2px(3)
        )

        mDistance = typeArray.getDimension(
            R.styleable.CircularRateView_remindTextDistance,
            DisplayUtils.dip2px(10)
        )
        mIsShowRemindText =
            typeArray.getBoolean(R.styleable.CircularRateView_isShowRemindText, false)

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
     * 设值是否显示提示内容
     */
    fun setIsShowRemind(isShowRemindText: Boolean) {
        this.mIsShowRemindText = isShowRemindText
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

        mCirclePaint = Paint()
        mCirclePaint.isAntiAlias = true
        mCirclePaint.style = Paint.Style.FILL
        mCirclePaint.color = mCircleColor

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
            widthSize + paddingBottom + paddingTop
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
//        mRadius = (measuredWidth / 5).toFloat()

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
            if (mIsShowRemindText) {
                drawRemind(it.ringRemind, currentAngle + angle / 2, canvas)
            }
            currentAngle += angle
        }
        drawCenterText(canvas)
    }

    /**
     * 绘制remind内容
     * @param textRemind 提示文本
     * @param middleAngle 圆弧中间角度
     */
    private fun drawRemind(textRemind: String, middleAngle: Float, canvas: Canvas) {
        drawDot(middleAngle, canvas)
        drawRemindText(textRemind, middleAngle, canvas)
        drawBrokenLine(PaintUtils.getFontLength(mTextPaint, textRemind), middleAngle, canvas)

    }

    /**
     * 绘制折线
     * @param remindTextLength 文本长度
     * @param middleAngle 中间角度
     */
    private fun drawBrokenLine(remindTextLength: Float, middleAngle: Float, canvas: Canvas) {
        mTextPaint.color = mBrokenLineColor
        val toRadians = Math.toRadians(middleAngle.toDouble())
        mBrokenLineArray[0] =
            mCenterX + (mRadius + mRingWidth + mCircleRadius) * cos(toRadians).toFloat()
        mBrokenLineArray[1] =
            mCenterX + (mRadius + mRingWidth + mCircleRadius) * sin(toRadians).toFloat()
        mBrokenLineArray[2] =
            mCenterX + (mRadius + mRingWidth + mDistance) * cos(toRadians).toFloat()
        mBrokenLineArray[3] =
            mCenterY + (mRadius + mRingWidth + mDistance) * sin(toRadians).toFloat()
        mBrokenLineArray[4] = mBrokenLineArray[2]
        mBrokenLineArray[5] = mBrokenLineArray[3]
        mBrokenLineArray[7] = mBrokenLineArray[3]
        if (middleAngle > -90 && middleAngle < 90) {
            //右半边
            mBrokenLineArray[6] = mBrokenLineArray[2] + remindTextLength
        } else if (middleAngle > 90 && middleAngle < 270) {
            //左半边
            mBrokenLineArray[6] = mBrokenLineArray[2] - remindTextLength
        }
        canvas.drawLines(mBrokenLineArray, mTextPaint)
    }

    /**
     * 绘制中间文本
     */
    private fun drawCenterText(canvas: Canvas) {
        mTextPaint.textAlign = Paint.Align.CENTER
        mTextPaint.color = mFirstTextColor
        mTextPaint.textSize = mFirstTextSize
        if (mSecondText.isNullOrEmpty()) {  //不存在副文本
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
                mSecondText!!,
                mCenterX,
                mCenterY + PaintUtils.getFontHeight(mTextPaint) + 5f,
                mTextPaint
            )
        }
    }


    /**
     * 绘制小圆点
     */
    private fun drawDot(middleAngle: Float, canvas: Canvas) {
        val toRadians = Math.toRadians(middleAngle.toDouble())
        val textX = (mRadius + mRingWidth) * cos(toRadians).toFloat()
        val textY = (mRadius + mRingWidth) * sin(toRadians).toFloat()
        canvas.drawCircle(mCenterX + textX, mCenterY + textY, mCircleRadius, mCirclePaint)
    }


    /**
     * 绘制提示文字
     * @param remindText  提示文字
     * @param middleAngle 中间角度
     */
    private fun drawRemindText(remindText: String, middleAngle: Float, canvas: Canvas) {
        mTextPaint.color = mRemindTextColor
        mTextPaint.textSize = mRemindTextSize

        val toRadians = Math.toRadians(middleAngle.toDouble())
        val textX = (mRadius + mRingWidth + mDistance) * cos(toRadians).toFloat()
        val textY = (mRadius + mRingWidth + mDistance) * sin(toRadians).toFloat() - 10
        if (middleAngle > -90 && middleAngle < 90) {
            //右半边
            mTextPaint.textAlign = Paint.Align.LEFT
        } else if (middleAngle > 90 && middleAngle < 270) {
            //左半边
            mTextPaint.textAlign = Paint.Align.RIGHT
        }
        canvas.drawText(remindText, mCenterX + textX, mCenterY + textY, mTextPaint)
    }

}

