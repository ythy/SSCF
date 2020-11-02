package com.mx.cosmo.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.view.View.OnTouchListener
import android.widget.ListView


class NestedListView(context: Context, attrs: AttributeSet) : ListView(context, attrs), OnTouchListener,
    AbsListView.OnScrollListener {

    init {
        setOnScrollListener(this)
        setOnTouchListener(this)
    }

    override fun onScroll( view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {}

    override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var newHeight = 0
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        if (heightMode != MeasureSpec.EXACTLY) {
            val listAdapter = adapter
            if (listAdapter != null && !listAdapter.isEmpty) {
                var listPosition = 0
                while (listPosition < listAdapter.count && listPosition < MAXIMUM_LIST_ITEMS_VIEWABLE) {
                    val listItem = listAdapter.getView(listPosition, null, this)
                    //now it will not throw a NPE if listItem is a ViewGroup instance
                    if (listItem is ViewGroup) {
                        listItem.setLayoutParams(
                            LayoutParams(
                                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT
                            )
                        )
                    }
                    listItem.measure(widthMeasureSpec, heightMeasureSpec)
                    newHeight += listItem.measuredHeight
                    listPosition++
                }
                newHeight += dividerHeight * listPosition
            }
            if (heightMode == MeasureSpec.AT_MOST && newHeight > heightSize) {
                if (newHeight > heightSize) {
                    newHeight = heightSize
                }
            }
        } else {
            newHeight = measuredHeight
        }
        setMeasuredDimension(measuredWidth, newHeight)
    }

    override fun onTouch(v: View, event: MotionEvent): Boolean {
        return false
    }

    companion object {
        private const val MAXIMUM_LIST_ITEMS_VIEWABLE = 99
    }
}