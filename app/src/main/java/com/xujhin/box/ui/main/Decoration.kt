package com.xujhin.box.ui.main

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.util.SparseArray
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


abstract class StickyHeaderDecoration(bottomSpace: Int) : ItemDecoration() {
    private val mHeaderTxtPaint: Paint
    private val mHeaderContentPaint: Paint
    private val normalSpace = 0
    private var bottomSpace = 0
    private var headerHeight = 136 //头部高度
    private var textPaddingLeft = 50 //头部文字左边距
    private var textSize = 50
    private var textColor = Color.BLACK
    private var headerContentColor = -0x111112
    private val txtYAxis: Float
    private var mRecyclerView: RecyclerView? = null
    private var isInitHeight = false

    /**
     * 先调用getItemOffsets再调用onDraw
     */
    override fun getItemOffsets(outRect: Rect, itemView: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, itemView, parent, state)
        if (mRecyclerView == null) {
            mRecyclerView = parent
        }
        val childCount = mRecyclerView!!.layoutManager!!.itemCount
        if (headerDrawEvent != null && !isInitHeight) {
            val headerView = headerDrawEvent!!.getHeaderView(0)
            headerView
                .measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
                )
            headerHeight = headerView.measuredHeight
            isInitHeight = true
        }

        /*我们为每个不同头部名称的第一个item设置头部高度*/
        val pos = parent.getChildAdapterPosition(itemView) //获取当前itemView的位置
        val curHeaderName = getHeaderName(pos) ?: return //根据pos获取要悬浮的头部名
        if (pos == 0 || curHeaderName != getHeaderName(pos - 1)) {
            //如果当前位置为0，或者与上一个item头部名不同的，都腾出头部空间
            //设置itemView PaddingTop的距离
            if (curHeaderName == "") {
                outRect.top = 0
            } else {
                outRect.top = headerHeight
            }
        }
        if (pos == childCount - 1) {
            outRect.bottom = bottomSpace
        } else {
            outRect.bottom = normalSpace
        }
    }

    abstract fun getHeaderName(pos: Int): String

    //记录每个头部和悬浮头部的坐标信息【用于点击事件】
    private val stickyHeaderPosArray = SparseArray<Int>()
    override fun onDrawOver(canvas: Canvas, recyclerView: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(canvas, recyclerView, state)
        if (mRecyclerView == null) {
            mRecyclerView = recyclerView
        }
        stickyHeaderPosArray.clear()
        val childCount = recyclerView.childCount //获取屏幕上可见的item数量
        val left = recyclerView.left + recyclerView.paddingLeft
        val right = recyclerView.right - recyclerView.paddingRight
        var firstHeaderName: String? = null
        var firstPos = 0
        var translateTop = 0 //绘制悬浮头部的偏移量
        /*for循环里面绘制每个分组的头部*/for (i in 0 until childCount) {
            val childView = recyclerView.getChildAt(i)
            val pos = recyclerView.getChildAdapterPosition(childView) //获取当前view在Adapter里的pos
            val curHeaderName = getHeaderName(pos) //根据pos获取要悬浮的头部名
            if (i == 0) {
                firstHeaderName = curHeaderName
                firstPos = pos
            }
            if (curHeaderName == null) continue  //如果headerName为空，跳过此次循环
            val viewTop = childView.top + recyclerView.paddingTop
            if (pos == 0 || curHeaderName != getHeaderName(pos - 1)) {
                if (curHeaderName == "") {
                    canvas.drawRect(
                        left.toFloat(),
                        viewTop.toFloat(),
                        right.toFloat(),
                        viewTop.toFloat(),
                        mHeaderContentPaint
                    )
                } else {
                    canvas.drawRect(
                        left.toFloat(),
                        (viewTop - headerHeight).toFloat(),
                        right.toFloat(),
                        viewTop.toFloat(),
                        mHeaderContentPaint
                    )
                    canvas.drawText(
                        curHeaderName,
                        (
                                left + textPaddingLeft).toFloat(),
                        viewTop - headerHeight / 2 + txtYAxis,
                        mHeaderTxtPaint
                    )
                }
                //此判断是刚好2个头部碰撞，悬浮头部就要偏移
                if (headerHeight < viewTop && viewTop <= 2 * headerHeight) {
                    translateTop = viewTop - 2 * headerHeight
                }
                stickyHeaderPosArray.put(pos, viewTop) //将头部信息放进array
            }
        }
        if (firstHeaderName == null) return
        canvas.save()
        canvas.translate(0f, translateTop.toFloat())
        /*绘制悬浮的头部*/if ("" == firstHeaderName) {
            canvas.drawRect(left.toFloat(), 0f, right.toFloat(), 0f, mHeaderContentPaint)
        } else {
            canvas.drawRect(left.toFloat(), 0f, right.toFloat(), headerHeight.toFloat(), mHeaderContentPaint)
        }
        canvas.drawText(
            firstHeaderName,
            (left + textPaddingLeft).toFloat(),
            headerHeight / 2 + txtYAxis,
            mHeaderTxtPaint
        )
        canvas.restore()
    }

    private val headViewMap: MutableMap<Int, View> = HashMap()

    interface OnHeaderClickListener {
        fun headerClick(pos: Int)
    }

    private var headerClickEvent: OnHeaderClickListener? = null
    fun setOnHeaderClickListener(headerClickListener: OnHeaderClickListener?) {
        headerClickEvent = headerClickListener
    }

    private val gestureListener: GestureDetector.OnGestureListener = object : GestureDetector.OnGestureListener {
        override fun onDown(e: MotionEvent): Boolean {
            return false
        }

        override fun onShowPress(e: MotionEvent) {}
        override fun onSingleTapUp(e: MotionEvent): Boolean {
            for (i in 0 until stickyHeaderPosArray.size()) {
                val value = stickyHeaderPosArray.valueAt(i)
                val y = e.y
                if (value - headerHeight <= y && y <= value) { //如果点击到分组头
                    if (headerClickEvent != null) {
                        headerClickEvent!!.headerClick(stickyHeaderPosArray.keyAt(i))
                    }
                    return true
                }
            }
            return false
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            return false
        }

        override fun onLongPress(e: MotionEvent) {}
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            return false
        }
    }
    private var headerDrawEvent: OnDecorationHeadDraw? = null

    interface OnDecorationHeadDraw {
        fun getHeaderView(pos: Int): View
    }

    /**
     * 只是用来绘制，不能做其他处理/点击事件等
     */
    fun setOnDecorationHeadDraw(decorationHeadDraw: OnDecorationHeadDraw?) {
        headerDrawEvent = decorationHeadDraw
    }

    private val imgDrawableMap: MutableMap<String, Drawable> = HashMap()

    init {
        this.bottomSpace = bottomSpace
        mHeaderTxtPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHeaderTxtPaint.color = textColor
        mHeaderTxtPaint.textSize = textSize.toFloat()
        mHeaderTxtPaint.textAlign = Paint.Align.LEFT
        mHeaderContentPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mHeaderContentPaint.color = headerContentColor
        val fontMetrics = mHeaderTxtPaint.fontMetrics
        val total = -fontMetrics.ascent + fontMetrics.descent
        txtYAxis = total / 2 - fontMetrics.descent
    }

    private fun getImg(url: String): Drawable? {
        return imgDrawableMap[url]
    }

    fun onDestory() {
        headViewMap.clear()
        imgDrawableMap.clear()
        stickyHeaderPosArray.clear()
        mRecyclerView = null
        setOnHeaderClickListener(null)
        setOnDecorationHeadDraw(null)
    }

    fun setHeaderHeight(headerHeight: Int) {
        this.headerHeight = headerHeight
    }

    fun setTextPaddingLeft(textPaddingLeft: Int) {
        this.textPaddingLeft = textPaddingLeft
    }

    fun setTextSize(textSize: Int) {
        this.textSize = textSize
        mHeaderTxtPaint.textSize = textSize.toFloat()
    }

    fun setTextColor(textColor: Int) {
        this.textColor = textColor
        mHeaderTxtPaint.color = textColor
    }

    fun setHeaderContentColor(headerContentColor: Int) {
        this.headerContentColor = headerContentColor
        mHeaderContentPaint.color = headerContentColor
    }
}
