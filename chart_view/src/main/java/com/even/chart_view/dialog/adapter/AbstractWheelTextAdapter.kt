package com.even.chart_view.dialog.adapter

import android.content.Context
import android.graphics.Typeface
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 *  @author  Created by Even on 2020/6/10
 *  Email: emailtopan@163.com
 *
 */
abstract class AbstractWheelTextAdapter : AbstractWheelAdapter {
    var mViewLists = mutableListOf<View>()
    private var currentIndex = 0
    private var maxSize = 24f
    private var minSize = 14f


    var textColor = DEFAULT_TEXT_COLOR
    var textSize = maxSize
    var emptyItemResourceId = NO_RESOURCE
    var itemResourceId = NO_RESOURCE
    var itemTextResourceId = NO_RESOURCE


    constructor() : this(TEXT_VIEW_ITEM_RESOURCE)
    constructor(itemResource: Int) : this(
        itemResource,
        NO_RESOURCE,
        0,
        24f,
        14f
    )

    constructor(
        itemResource: Int,
        itemTextResource: Int,
        currentIndex: Int,
        maxSize: Float, minSize: Float
    ) : super() {
        this.itemResourceId = itemResource
        this.itemTextResourceId = itemTextResource
        this.currentIndex = currentIndex
        this.maxSize = maxSize
        this.minSize = minSize
    }

    abstract fun getItemText(index: Int): CharSequence?

    override fun getItem(
        index: Int,
        currentItem: Int,
        convertView: View?,
        parent: ViewGroup
    ): View? {
        var view = convertView
        if (index in 0 until getItemsCount()) {
            if (view == null) {
                view = getView(itemResourceId, parent)
            }
            var textView = getTextView(view, itemTextResourceId)
            textView?.let {
                if (!mViewLists.contains(it)) {
                    mViewLists.add(it)
                }

                var text = getItemText(index)
                if (text == null) text = ""
                textView.text = text

                if (index == currentIndex) {
                    textView.textSize = maxSize
                } else {
                    textView.textSize = minSize
                }

                if (itemResourceId == TEXT_VIEW_ITEM_RESOURCE) {
                    configureTextView(textView)
                }
            }

            return view
        }
        return null
    }

    override fun getEmptyItem(convertView: View?, parent: ViewGroup): View? {
        var view = convertView
        if (view == null) {
            view = getView(emptyItemResourceId, parent)
        }
        if (emptyItemResourceId == TEXT_VIEW_ITEM_RESOURCE && view is TextView) {
            configureTextView(view)
        }
        return view
    }

    /**
     * 设置Text View字体样式
     */
    private fun configureTextView(view: TextView) {
        view.setTextColor(textColor)
        view.gravity = Gravity.CENTER
        view.textSize = textSize
        view.setTypeface(Typeface.SANS_SERIF, Typeface.BOLD)
    }


    /**
     * 根据资源加载TextView，资源为空得时候使用默认资源
     */
    private fun getTextView(view: View?, textResource: Int): TextView? {
        return if (textResource == NO_RESOURCE && view is TextView) {
            view
        } else if (textResource != NO_RESOURCE) {
            view?.findViewById<TextView>(textResource)
        } else {
            null
        }
    }


    /**
     * 加载指定资源得View
     */
    private fun getView(resource: Int, parent: ViewGroup): View? {
        return when (resource) {
            NO_RESOURCE -> null
            TEXT_VIEW_ITEM_RESOURCE -> TextView(parent.context)
            else -> (parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                resource,
                parent,
                false
            )
        }

    }

    companion object {
        //text资源
        const val TEXT_VIEW_ITEM_RESOURCE = -1

        const val NO_RESOURCE = 0

        //默认文字颜色
        const val DEFAULT_TEXT_COLOR = 0x333333
    }
}