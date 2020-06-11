package com.even.chart_view.dialog.views

import android.content.Context
import android.database.DataSetObserver
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.GradientDrawable.Orientation
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.Interpolator
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.even.chart_view.R
import com.even.chart_view.dialog.adapter.WheelViewAdapter
import com.even.chart_view.dialog.imp.ScrollingListener
import com.even.chart_view.dialog.imp.WheelChangedListener
import com.even.chart_view.dialog.imp.WheelClickedListener
import com.even.chart_view.dialog.imp.WheelScrollListener
import java.util.*

/**
 *  @author  Created by Even on 2020/6/8
 *  Email: emailtopan@163.com
 *  选择控件
 */
class WheelView : View {

    /**
     * Top and bottom shadows colors
     */
    //private static final int[] SHADOWS_COLORS = new int[] { 0xeeffffff, 0xeaffffff, 0x33ffffff };
    private val SHADOWS_COLORS = intArrayOf(0x33ffffff, 0x33ffffff, 0x33ffffff)

    /**
     * Top and bottom items offset (to hide that)
     */
    private val ITEM_OFFSET_PERCENT = 10

    /**
     * Left and right padding value
     */
    private val PADDING = 10

    /**
     * Default count of visible items
     */
    private val DEF_VISIBLE_ITEMS = 5

    // Wheel Values
    private var currentItem = 0

    // Count of visible items
    private var visibleItems = DEF_VISIBLE_ITEMS

    // Item height
    private var itemHeight = 0

    // Center Line
    private var centerDrawable: Drawable? = null

    // Shadows drawables
    private var topShadow: GradientDrawable? = null
    private var bottomShadow: GradientDrawable? = null

    // Scrolling
    private var scroller: WheelScroller? = null
    private var isScrollingPerformed = false
    private var scrollingOffset = 0

    // Cyclic
    private var isCyclic = false

    // Items layout
    private var itemsLayout: LinearLayout? = null

    // The number of first item in layout
    private var firstItem = 0

    // View adapter
    private var viewAdapter: WheelViewAdapter? = null

    // Recycle
    private val recycle = WheelRecycle(this)

    // Listeners
    private val changingListeners: MutableList<WheelChangedListener> =
        LinkedList<WheelChangedListener>()
    private val scrollingListeners: MutableList<WheelScrollListener> =
        LinkedList<WheelScrollListener>()
    private val clickingListeners: MutableList<WheelClickedListener> =
        LinkedList<WheelClickedListener>()

    /**
     * Constructor
     */
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int
    ) : super(context, attrs, defStyle) {

        initData(context)
    }

    /**
     * Constructor
     */
    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {

        initData(context)
    }

    /**
     * Constructor
     */
    constructor(context: Context) : super(context) {

        initData(context)
    }

    /**
     * Initializes class data
     *
     * @param context the context
     */
    private fun initData(context: Context) {
        scroller = WheelScroller(getContext(), scrollingListener)
    }

    // Scrolling listener
    var scrollingListener: ScrollingListener = object : ScrollingListener {
        override fun onStarted() {
            isScrollingPerformed = true
            notifyScrollingListenersAboutStart()
        }

        override fun onScroll(distance: Int) {
            doScroll(distance)
            val height = height
            if (scrollingOffset > height) {
                scrollingOffset = height
                scroller!!.stopScrolling()
            } else if (scrollingOffset < -height) {
                scrollingOffset = -height
                scroller!!.stopScrolling()
            }
        }

        override fun onFinished() {
            if (isScrollingPerformed) {
                notifyScrollingListenersAboutEnd()
                isScrollingPerformed = false
            }
            scrollingOffset = 0
            invalidate()
        }

        override fun onJustify() {
            if (Math.abs(scrollingOffset) > WheelScroller.MIN_ITEM_FOR_SCROLLING) {
                scroller!!.scroll(scrollingOffset, 0)
            }
        }
    }

    /**
     * Set the the specified scrolling interpolator
     *
     * @param interpolator the interpolator
     */
    fun setInterpolator(interpolator: Interpolator) {
        scroller!!.setInterpolator(context, interpolator)
    }

    /**
     * Gets count of visible items
     *
     * @return the count of visible items
     */
    fun getVisibleItems(): Int {
        return visibleItems
    }

    /**
     * Sets the desired count of visible items. Actual amount of visible items
     * depends on wheel layout parameters. To apply changes and rebuild mContentView
     * call measure().
     *
     * @param count the desired count for visible items
     */
    fun setVisibleItems(count: Int) {
        visibleItems = count
    }

    /**
     * Gets mContentView adapter
     *
     * @return the mContentView adapter
     */
    fun getViewAdapter(): WheelViewAdapter? {
        return viewAdapter
    }

    // Adapter listener
    private val dataObserver: DataSetObserver = object : DataSetObserver() {
        override fun onChanged() {
            invalidateWheel(false)
        }

        override fun onInvalidated() {
            invalidateWheel(true)
        }
    }

    /**
     * Sets mContentView adapter. Usually new adapters contain different views, so it
     * needs to rebuild mContentView by calling measure().
     *
     * @param viewAdapter the mContentView adapter
     */
    fun setViewAdapter(viewAdapter: WheelViewAdapter?) {
        if (this.viewAdapter != null) {
            this.viewAdapter!!.unregisterDataSetObserver(dataObserver)
        }
        this.viewAdapter = viewAdapter
        if (this.viewAdapter != null) {
            this.viewAdapter!!.registerDataSetObserver(dataObserver)
        }
        invalidateWheel(true)
    }

    /**
     * Adds wheel changing listener
     *
     * @param listener the listener
     */
    fun addChangingListener(listener: WheelChangedListener) {
        changingListeners.add(listener)
    }

    /**
     * Removes wheel changing listener
     *
     * @param listener the listener
     */
    fun removeChangingListener(listener: WheelChangedListener) {
        changingListeners.remove(listener)
    }

    /**
     * Notifies changing listeners
     *
     * @param oldValue the old wheel value
     * @param newValue the new wheel value
     */
    protected fun notifyChangingListeners(oldValue: Int, newValue: Int) {
        for (listener in changingListeners) {
            listener.onChanged(this, oldValue, newValue)
        }
    }

    /**
     * Adds wheel scrolling listener
     *
     * @param listener the listener
     */
    fun addScrollingListener(listener: WheelScrollListener) {
        scrollingListeners.add(listener)
    }

    /**
     * Removes wheel scrolling listener
     *
     * @param listener the listener
     */
    fun removeScrollingListener(listener: WheelScrollListener) {
        scrollingListeners.remove(listener)
    }

    /**
     * Notifies listeners about starting scrolling
     */
    protected fun notifyScrollingListenersAboutStart() {
        for (listener in scrollingListeners) {
            listener.onScrollingStarted(this)
        }
    }

    /**
     * Notifies listeners about ending scrolling
     */
    protected fun notifyScrollingListenersAboutEnd() {
        for (listener in scrollingListeners) {
            listener.onScrollingFinish(this)
        }
    }

    /**
     * Adds wheel clicking listener
     *
     * @param listener the listener
     */
    fun addClickingListener(listener: WheelClickedListener) {
        clickingListeners.add(listener)
    }

    /**
     * Removes wheel clicking listener
     *
     * @param listener the listener
     */
    fun removeClickingListener(listener: WheelClickedListener) {
        clickingListeners.remove(listener)
    }

    /**
     * Notifies listeners about clicking
     */
    protected fun notifyClickListenersAboutClick(item: Int) {
        for (listener in clickingListeners) {
            listener.onItemClick(this, item)
        }
    }

    /**
     * Gets current value
     *
     * @return the current value
     */
    fun getCurrentItem(): Int {
        return currentItem
    }

    /**
     * Sets the current item. Does nothing when index is wrong.
     *
     * @param index    the item index
     * @param animated the animation flag
     */
    fun setCurrentItem(index: Int, animated: Boolean) {
        var index = index
        if (viewAdapter == null || viewAdapter!!.getItemsCount() === 0) {
            return  // throw?
        }
        val itemCount = viewAdapter!!.getItemsCount()
        if (index < 0 || index >= itemCount) {
            if (isCyclic) {
                while (index < 0) {
                    index += itemCount
                }
                index %= itemCount
            } else {
                return  // throw?
            }
        }
        if (index != currentItem) {
            if (animated) {
                var itemsToScroll = index - currentItem
                if (isCyclic) {
                    val scroll =
                        itemCount + Math.min(index, currentItem) - Math.max(
                            index,
                            currentItem
                        )
                    if (scroll < Math.abs(itemsToScroll)) {
                        itemsToScroll = if (itemsToScroll < 0) scroll else -scroll
                    }
                }
                scroll(itemsToScroll, 0)
            } else {
                scrollingOffset = 0
                val old = currentItem
                currentItem = index
                notifyChangingListeners(old, currentItem)
                invalidate()
            }
        }
    }

    /**
     * Sets the current item w/o animation. Does nothing when index is wrong.
     *
     * @param index the item index
     */
    fun setCurrentItem(index: Int) {
        setCurrentItem(index, false)
    }

    /**
     * Tests if wheel is cyclic. That means before the 1st item there is shown
     * the last one
     *
     * @return true if wheel is cyclic
     */
    fun isCyclic(): Boolean {
        return isCyclic
    }

    /**
     * Set wheel cyclic flag
     *
     * @param isCyclic the flag to set
     */
    fun setCyclic(isCyclic: Boolean) {
        this.isCyclic = isCyclic
        invalidateWheel(false)
    }

    /**
     * Invalidates wheel
     *
     * @param clearCaches if true then cached views will be clear
     */
    fun invalidateWheel(clearCaches: Boolean) {
        if (clearCaches) {
            recycle.clearAll()
            if (itemsLayout != null) {
                itemsLayout!!.removeAllViews()
            }
            scrollingOffset = 0
        } else if (itemsLayout != null) {
            // cache all items
            recycle.recycleItems(itemsLayout!!, firstItem, ItemsRange())
        }
        invalidate()
    }

    /**
     * Initializes resources
     */
    private fun initResourcesIfNecessary() {
        if (centerDrawable == null) {
            centerDrawable =
                ContextCompat.getDrawable(context, R.drawable.wheel_val)
        }
        if (topShadow == null) {
            topShadow = GradientDrawable(Orientation.TOP_BOTTOM, SHADOWS_COLORS)
        }
        if (bottomShadow == null) {
            bottomShadow = GradientDrawable(Orientation.BOTTOM_TOP, SHADOWS_COLORS)
        }
        setBackgroundColor(Color.TRANSPARENT)
    }

    /**
     * Calculates desired height for layout
     *
     * @param layout the source layout
     * @return the desired layout height
     */
    private fun getDesiredHeight(layout: LinearLayout?): Int {
        if (layout != null && layout.getChildAt(0) != null) {
            itemHeight = layout.getChildAt(0).measuredHeight
        }
        val desired = itemHeight * visibleItems - itemHeight * ITEM_OFFSET_PERCENT / 50
        return Math.max(desired, suggestedMinimumHeight)
    }

    /**
     * Returns height of wheel item
     *
     * @return the item height
     */
    private fun getItemHeight(): Int {
        if (itemHeight != 0) {
            return itemHeight
        }
        if (itemsLayout != null && itemsLayout!!.getChildAt(0) != null) {
            itemHeight = itemsLayout!!.getChildAt(0).height
            return itemHeight
        }
        return height / visibleItems
    }

    /**
     * Calculates control width and creates text layouts
     *
     * @param widthSize the input layout width
     * @param mode      the layout mode
     * @return the calculated control width
     */
    private fun calculateLayoutWidth(widthSize: Int, mode: Int): Int {
        initResourcesIfNecessary()

        // TODO: make it static
        itemsLayout!!.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        itemsLayout!!.measure(
            MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.UNSPECIFIED),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        var width = itemsLayout!!.measuredWidth
        if (mode == MeasureSpec.EXACTLY) {
            width = widthSize
        } else {
            width += 2 * PADDING

            // Check against our minimum width
            width = Math.max(width, suggestedMinimumWidth)
            if (mode == MeasureSpec.AT_MOST && widthSize < width) {
                width = widthSize
            }
        }
        itemsLayout!!.measure(
            MeasureSpec.makeMeasureSpec(width - 2 * PADDING, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        return width
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        buildViewForMeasuring()
        val width = calculateLayoutWidth(widthSize, widthMode)
        var height: Int
        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize
        } else {
            height = getDesiredHeight(itemsLayout)
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize)
            }
        }
        setMeasuredDimension(width, height)
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        layout(r - l, b - t)
    }

    /**
     * Sets layouts width and height
     *
     * @param width  the layout width
     * @param height the layout height
     */
    private fun layout(width: Int, height: Int) {
        val itemsWidth = width - 2 * PADDING
        itemsLayout!!.layout(0, 0, itemsWidth, height)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (viewAdapter != null && viewAdapter!!.getItemsCount() > 0) {
            updateView()
            drawItems(canvas)
            drawCenterRect(canvas)
        }
        drawShadows(canvas)
    }

    /**
     * Draws shadows on top and bottom of control
     *
     * @param canvas the canvas for drawing
     */
    private fun drawShadows(canvas: Canvas) {
        val height = (1.5 * getItemHeight()).toInt()
        topShadow!!.setBounds(0, 0, width, height)
        topShadow!!.draw(canvas)
        bottomShadow!!.setBounds(0, getHeight() - height, width, getHeight())
        bottomShadow!!.draw(canvas)
    }

    /**
     * Draws items
     *
     * @param canvas the canvas for drawing
     */
    private fun drawItems(canvas: Canvas) {
        canvas.save()
        viewAdapter!!.moveChange(itemsLayout!!, currentItem)
        val top =
            (currentItem - firstItem) * getItemHeight() + (getItemHeight() - height) / 2
        canvas.translate(PADDING.toFloat(), -top + scrollingOffset.toFloat())
        itemsLayout!!.draw(canvas)
        canvas.restore()
    }

    /**
     * Draws rect for current value
     *
     * @param canvas the canvas for drawing
     */
    private fun drawCenterRect(canvas: Canvas) {
        val center = height / 2
        val offset = (getItemHeight() / 2 * 1.2).toInt()
        centerDrawable!!.setBounds(0, center - offset, width, center + offset)
        centerDrawable!!.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled || getViewAdapter() == null) {
            return true
        }
        when (event.action) {
            MotionEvent.ACTION_MOVE -> if (parent != null) {
                parent.requestDisallowInterceptTouchEvent(true)
            }
            MotionEvent.ACTION_UP -> if (!isScrollingPerformed) {
                var distance = event.y.toInt() - height / 2
                if (distance > 0) {
                    distance += getItemHeight() / 2
                } else {
                    distance -= getItemHeight() / 2
                }
                val items = distance / getItemHeight()
                if (items != 0 && isValidItemIndex(currentItem + items)) {
                    notifyClickListenersAboutClick(currentItem + items)
                }
            }
            else -> {
            }
        }
        return scroller!!.onTouchEvent(event)
    }

    /**
     * Scrolls the wheel
     *
     * @param delta the scrolling value
     */
    private fun doScroll(delta: Int) {
        scrollingOffset += delta
        val itemHeight = getItemHeight()
        var count = scrollingOffset / itemHeight
        var pos = currentItem - count
        val itemCount = viewAdapter!!.getItemsCount()
        var fixPos = scrollingOffset % itemHeight
        if (Math.abs(fixPos) <= itemHeight / 2) {
            fixPos = 0
        }
        if (isCyclic && itemCount > 0) {
            if (fixPos > 0) {
                pos--
                count++
            } else if (fixPos < 0) {
                pos++
                count--
            }
            // fix position by rotating
            while (pos < 0) {
                pos += itemCount
            }
            pos %= itemCount
        } else {
            //
            if (pos < 0) {
                count = currentItem
                pos = 0
            } else if (pos >= itemCount) {
                count = currentItem - itemCount + 1
                pos = itemCount - 1
            } else if (pos > 0 && fixPos > 0) {
                pos--
                count++
            } else if (pos < itemCount - 1 && fixPos < 0) {
                pos++
                count--
            }
        }
        val offset = scrollingOffset
        if (pos != currentItem) {
            setCurrentItem(pos, false)
        } else {
            invalidate()
        }

        // update offset
        scrollingOffset = offset - count * itemHeight
        if (scrollingOffset > height) {
            scrollingOffset = scrollingOffset % height + height
        }
    }

    /**
     * Scroll the wheel
     *
     *
     * items to scroll
     *
     * @param time scrolling duration
     */
    fun scroll(itemsToScroll: Int, time: Int) {
        val distance = itemsToScroll * getItemHeight() - scrollingOffset
        scroller!!.scroll(distance, time)
    }

    /**
     * Calculates range for wheel items
     *
     * @return the items range
     */
    private fun getItemsRange(): ItemsRange? {
        if (getItemHeight() == 0) {
            return null
        }
        var first = currentItem
        var count = 1
        while (count * getItemHeight() < height) {
            first--
            count += 2 // top + bottom items
        }
        if (scrollingOffset != 0) {
            if (scrollingOffset > 0) {
                first--
            }
            count++

            // process empty items above the first or below the second
            val emptyItems = scrollingOffset / getItemHeight()
            first -= emptyItems
            count += Math.asin(emptyItems.toDouble()).toInt()
        }
        return ItemsRange(first, count)
    }

    /**
     * Rebuilds wheel items if necessary. Caches all unused items.
     *
     * @return true if items are rebuilt
     */
    private fun rebuildItems(): Boolean {
        var updated = false
        val range = getItemsRange()
        if (itemsLayout != null) {
            val first = recycle.recycleItems(itemsLayout!!, firstItem, range!!)
            updated = firstItem != first
            firstItem = first
        } else {
            createItemsLayout()
            updated = true
        }
        if (!updated) {
            updated =
                firstItem != range!!.getFirst() || itemsLayout!!.childCount != range.getCount()
        }
        if (firstItem > range!!.getFirst() && firstItem <= range.getLast()) {
            for (i in firstItem - 1 downTo range.getFirst()) {
                if (!addViewItem(i, true)) {
                    break
                }
                firstItem = i
            }
        } else {
            firstItem = range.getFirst()
        }
        var first = firstItem
        for (i in itemsLayout!!.childCount until range.getCount()) {
            if (!addViewItem(firstItem + i, false) && itemsLayout!!.childCount == 0) {
                first++
            }
        }
        firstItem = first
        return updated
    }

    /**
     * Updates mContentView. Rebuilds items and label if necessary, recalculate items
     * sizes.
     */
    private fun updateView() {
        if (rebuildItems()) {
            calculateLayoutWidth(width, MeasureSpec.EXACTLY)
            layout(width, height)
        }
    }

    /**
     * Creates item layouts if necessary
     */
    private fun createItemsLayout() {
        if (itemsLayout == null) {
            itemsLayout = LinearLayout(context)
            itemsLayout!!.orientation = LinearLayout.VERTICAL
        }
    }

    /**
     * Builds mContentView for measuring
     */
    private fun buildViewForMeasuring() {
        // clear all items
        if (itemsLayout != null) {
            recycle.recycleItems(itemsLayout!!, firstItem, ItemsRange())
        } else {
            createItemsLayout()
        }

        // add views
        val addItems = visibleItems / 2
        for (i in currentItem + addItems downTo currentItem - addItems) {
            if (addViewItem(i, true)) {
                firstItem = i
            }
        }
    }

    /**
     * Adds mContentView for item to items layout
     *
     * @param index the item index
     * @param first the flag indicates if mContentView should be first
     * @return true if corresponding item exists and is added
     */
    private fun addViewItem(index: Int, first: Boolean): Boolean {
        val view = getItemView(index)
        if (view != null) {
            view.tag = index
            if (first) {
                itemsLayout!!.addView(view, 0)
            } else {
                itemsLayout!!.addView(view)
            }
            return true
        }
        return false
    }

    /**
     * Checks whether intem index is valid
     *
     * @param index the item index
     * @return true if item index is not out of bounds or the wheel is cyclic
     */
    private fun isValidItemIndex(index: Int): Boolean {
        return viewAdapter != null && viewAdapter!!.getItemsCount() > 0 && (isCyclic || index >= 0 && index < viewAdapter!!.getItemsCount())
    }

    /**
     * Returns mContentView for specified item
     *
     * @param index the item index
     * @return item mContentView or empty mContentView if index is out of bounds
     */
    private fun getItemView(index: Int): View? {
        var index = index
        if (viewAdapter == null || viewAdapter!!.getItemsCount() === 0) {
            return null
        }
        val count = viewAdapter!!.getItemsCount()
        if (!isValidItemIndex(index)) {
            return viewAdapter!!.getEmptyItem(recycle.getEmptyItem(), itemsLayout!!)
        } else {
            while (index < 0) {
                index = count + index
            }
        }
        index %= count
        return viewAdapter!!.getItem(index, getCurrentItem(), recycle.getItem(), itemsLayout!!)
    }

    /**
     * Stops scrolling
     */
    fun stopScrolling() {
        scroller!!.stopScrolling()
    }
}