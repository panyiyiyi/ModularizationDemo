package com.even.chart_view.dialog.base

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.DialogFragment
import com.even.chart_view.R
import com.even.chart_view.bean.DialogTitleBarBean

/**
 *  @author  Created by Even on 2020/6/10
 *  Email: emailtopan@163.com
 *  DialogFragment 基类封装
 */
abstract class BaseDialogFragment : DialogFragment() {
    private var titleBarView: View? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val window = dialog?.window
        //去掉dialog 默认的padding
        window!!.decorView.setPadding(
            0,
            window.decorView.paddingTop,
            0,
            window.decorView.paddingBottom
        )
        val layoutParams = window.attributes
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
        layoutParams.height = getScreenRate()
        layoutParams.gravity = Gravity.BOTTOM
        layoutParams.windowAnimations = R.style.dialog_fragment_anim
        window.attributes = layoutParams
        window.setBackgroundDrawable(ColorDrawable())
        this.isCancelable = isCancelDialogOnOutSize()
        if (userDefaultTitleBar()) {
            val linearLayout = LinearLayout(context)
            linearLayout.setBackgroundColor(Color.WHITE)
            var layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            titleBarView = inflater.inflate(getTitleBarView(), container, false)
            linearLayout.orientation = LinearLayout.VERTICAL
            linearLayout.addView(titleBarView, layoutParams)

            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            val contentView = inflater.inflate(getContentView(), container, false)

            linearLayout.addView(contentView, layoutParams)
            initView(linearLayout)
            initData()
            return linearLayout
        } else {
            val view = inflater.inflate(getContentView(), container, false)
            initView(view)
            initData()
            return view
        }
    }

    /**
     * 初始化标题栏
     */
    fun initTitleBar(title: String): DialogTitleBarBean? {
        return initTitleBar(title, "")
    }

    /**
     * 初始化标题栏
     */
    fun initTitleBar(title: String, rightText: String): DialogTitleBarBean? {
        titleBarView?.let {
            val titleBar = DialogTitleBarBean()
            titleBar.backImage = it.findViewById(R.id.ivClose)
            titleBar.titleText = it.findViewById(R.id.tvTitle)
            titleBar.rightText = it.findViewById(R.id.tvRight)

            titleBar.titleText.text = title
            titleBar.rightText.text = rightText
            titleBar.backImage.setOnClickListener { dismissAllowingStateLoss() }
            if (rightText.isEmpty()) {
                titleBar.rightText.visibility = View.INVISIBLE
            } else {
                titleBar.rightText.visibility = View.VISIBLE
            }
            return titleBar
        }
        return null
    }

    /**
     * 设置标题布局
     * 修改标题布局直接重写该方法
     */
    open fun getTitleBarView() = R.layout.view_title_bar_dialog

    open fun initData() {}

    /**
     * 点击外部区域能否取消
     */
    open fun isCancelDialogOnOutSize() = true

    /**
     * 弹窗比例，默认是自适应，如果要设置为屏幕比例则修改这个比例
     */
    open fun getScreenRate(): Int = WindowManager.LayoutParams.WRAP_CONTENT

    /**
     * 是否使用默认标题栏，如果不使用，则重写此方法，返回false即可
     */
    open fun userDefaultTitleBar(): Boolean {
        return true
    }

    /**
     * 初始化View
     */
    abstract fun initView(view: View)


    /**
     * layoutId
     */
    abstract fun getContentView(): Int

}