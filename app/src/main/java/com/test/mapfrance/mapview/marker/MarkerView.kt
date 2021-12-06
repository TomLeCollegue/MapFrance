package com.test.mapfrance.mapview.marker

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.test.mapfrance.mapview.Region

abstract class MarkerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    abstract val layoutRes: Int

    init {
        initView()
    }

    private fun initView() {
        inflate(context, layoutRes, this)
    }

    open fun bindView(region: Region) {}

}
