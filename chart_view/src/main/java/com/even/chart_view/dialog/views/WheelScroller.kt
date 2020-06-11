package com.even.chart_view.dialog.views

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.Interpolator
import android.widget.Scroller
import com.even.chart_view.dialog.imp.ScrollingListener
import kotlin.math.abs

/**
 *  @author  Created by Even on 2020/6/8
 *  Email: emailtopan@163.com
 *  wheel滑动处理类
 */
class WheelScroller {
    private lateinit var mGestureDetector: GestureDetector
    private lateinit var mScroller: Scroller
    private lateinit var mListener: ScrollingListener

    //上一次滑动的Y轴距离
    private var mLastScrollY = 0

    //上一次触摸的Y点
    private var mLastTouchedY = 0f

    //是否在滑动
    private var mIsScrollingPerformed = false


    constructor(context: Context, listener: ScrollingListener) {
        mGestureDetector = GestureDetector(context, mGestureListener)
        mGestureDetector.setIsLongpressEnabled(false)

        mScroller = Scroller(context)
        this.mListener = listener
    }

    /**
     * 设置插值器
     */
    fun setInterpolator(context: Context, interpolator: Interpolator) {
        mScroller.forceFinished(true)
        mScroller = Scroller(context, interpolator)
    }

    /**
     * 设置wheel滑动
     */
    fun scroll(distance: Int, time: Int) {
        mScroller.forceFinished(true)
        mLastScrollY = 0
        mScroller.startScroll(0, 0, 0, distance, if (time == 0) SCROLLING_DURATION else time)
        setNextMessage(MESSAGE_SCROLL)
        startScrolling()
    }

    fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                mLastTouchedY = event.y
                mScroller.forceFinished(true)
                clearMessages()
            }
            MotionEvent.ACTION_MOVE -> {
                val distanceY = event.y - mLastTouchedY
                if (distanceY != 0f) {
                    startScrolling()
                    mListener.onScroll(distanceY.toInt())
                    mLastTouchedY = event.y
                }
            }
        }
        if (mGestureDetector.onTouchEvent(event) && event.action == MotionEvent.ACTION_UP) {
            justify()
        }
        return true
    }


    private var mGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return true
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            mLastScrollY = 0
            val maxY = 0x7FFFFFFF
            val minY = -maxY
            mScroller.fling(0, mLastScrollY, 0, (-velocityY).toInt(), 0, 0, minY, maxY)
            setNextMessage(MESSAGE_SCROLL)
            return true
        }
    }

    /**
     * 发送消息到队列，需要清除队列中以前的消息
     */
    private fun setNextMessage(message: Int) {
        clearMessages()
        mAnimationHandler.sendEmptyMessage(message)
    }

    /**
     * 移除所有消息
     */
    private fun clearMessages() {
        mAnimationHandler.removeMessages(MESSAGE_SCROLL)
        mAnimationHandler.removeMessages(MESSAGE_JUSTIFY)
    }


    private val mAnimationHandler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            mScroller.computeScrollOffset()
            var currY = mScroller.currY
            //偏移
            val delta = mLastScrollY - currY
            mLastScrollY = currY
            if (delta != 0) {
                mListener.onScroll(delta)
            }
            if (abs(currY - mScroller.finalY) < MIN_ITEM_FOR_SCROLLING) {
                currY = mScroller.finalY
                mScroller.forceFinished(true)
            }
            if (!mScroller.isFinished) {
                sendEmptyMessage(msg.what)
            } else if (msg.what == MESSAGE_SCROLL) {
                justify()
            } else {
                finishScrolling()
            }
        }
    }

    /**
     * 执行结束
     */
    private fun justify() {
        mListener.onJustify()
        setNextMessage(MESSAGE_JUSTIFY)
    }

    /**
     * 停止滑动
     */
    fun stopScrolling() {
        mScroller.forceFinished(true)
    }


    /**
     * 开始滑动
     */
    private fun startScrolling() {
        if (!mIsScrollingPerformed) {
            mIsScrollingPerformed = true
            mListener.onStarted()
        }
    }

    /**
     * 执行完成
     */
    private fun finishScrolling() {
        if (mIsScrollingPerformed) {
            mListener.onFinished()
            mIsScrollingPerformed = false
        }
    }


    companion object {
        //滑动时间
        const val SCROLLING_DURATION = 400

        //滚动最小Item数量
        const val MIN_ITEM_FOR_SCROLLING = 1


        //message  正在滑动
        const val MESSAGE_SCROLL = 0

        //滑动完成
        const val MESSAGE_JUSTIFY = 1

    }


}