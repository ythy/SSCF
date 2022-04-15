package com.mx.cosmo.common

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import android.view.ViewTreeObserver


class FloatTextView : TextView, ViewTreeObserver.OnGlobalLayoutListener {

    private lateinit var overflowText: String

    private var overflowTextListener: OverflowTextListener? = null

    var overflowTextViewId: Int = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun setText(text: CharSequence, type: TextView.BufferType) {
        super.setText(text, type)

        val vto = viewTreeObserver
        vto.addOnGlobalLayoutListener(this)
    }

    override fun onGlobalLayout() {

        // only try to calculate ellipsized text if MaxLines were set
        if (maxLines != Integer.MAX_VALUE) {
            // get the index of the Ellipsis in the line that has an elipsis
            val ellipIndex = layout.getLineEnd(maxLines - 1)

            // get all the text after the elipsis
            setOverflowText(
                text
                    .subSequence(ellipIndex, text.length) as String
            )

            viewTreeObserver.removeOnGlobalLayoutListener(this)
        }

        // only do crazy truncation if we need too.
        // this math isn't 100% accurate, but it should never fail.
        val allTextVisible = lineHeight * lineCount <= height

        if (!allTextVisible) {
            // get the index of the last visible line
            val lastLineIndex = height / lineHeight

            maxLines = lastLineIndex
        }
    }

    fun getOverflowText(): String {
        return overflowText
    }

    private fun setOverflowText(overflowText: String) {
        this.overflowText = overflowText
        invokeOverflowTextListener()
        updateOverflowTextView()
    }

    private fun updateOverflowTextView() {
        if (overflowTextViewId != 0) {
            if (context is Activity) {
                val overflowTextView = (context as Activity).findViewById<View>(overflowTextViewId) as TextView
                overflowTextView.text = getOverflowText()
            }
        }
    }

    fun setOverflowTextListener(listener: OverflowTextListener) {
        overflowTextListener = listener
    }

    private fun invokeOverflowTextListener() {
        val listener = overflowTextListener
        listener?.overflowTextCalculated(overflowText)
    }

    interface OverflowTextListener {
        fun overflowTextCalculated(overflowText: String)
    }
}