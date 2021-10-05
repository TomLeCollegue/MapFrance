package com.test.mapfrance

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.richpath.RichPath
import com.richpath.RichPathView
import com.richpathanimator.RichPathAnimator
import java.lang.Float.min

class MapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var mapPath: RichPathView

    var regionBackgroundColor: Int = Color.BLACK

    var isRegionToggleAnimated: Boolean = true

    var regionActiveColor: Int = Color.CYAN

    var regionStrokeColor: Int = Color.WHITE

    var getMarkerView: () -> MarkerView? = { null }
        set(value) {
            field = value
            resetMap()
        }

    var mapAdjustViewBound: Boolean = true

    var onClickRegion: (Boolean, Region?) -> Unit = { _, _ -> }

    @DrawableRes
    var mapDrawable: Int = R.drawable.france
        set(value) {
            field = value
            resetMap()
        }

    var isSelectByClickEnabled: Boolean = true
        set(value) {
            field = value
            toggleSelectByClick()
        }

    var isMultiSelectEnabled: Boolean = false

    var animationDuration: Long = 200L

    var regions: List<Region> = FrenchRegion.frenchRegion
        set(value) {
            field = value
            resetMap()
        }

    val selectedRegions: MutableList<Region> = mutableListOf()

    private val markerList: MutableList<View> = mutableListOf()

    init {
        addPathView()
        handleAttr(context, attrs)
    }

    private fun toggleSelectByClick() {
        if (isSelectByClickEnabled) {
            mapPath.setOnPathClickListener {
                onClickRegion(
                    toggleRegion(it),
                    getRegionFromRichPath(it)
                )
            }
        } else {
            mapPath.setOnPathClickListener(null)
        }
    }

    private fun resetMap() {
        removeAllViews()
        addPathView()
        setMapColors()
        selectedRegions.clear()
        toggleSelectByClick()
    }

    private fun addPathView() {
        //Add rich path view for show map svg
        mapPath = RichPathView(context)
        mapPath.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        mapPath.adjustViewBounds = mapAdjustViewBound
        mapPath.setVectorDrawable(mapDrawable)
        addView(mapPath)
    }

    private fun handleAttr(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MapView, 0, 0)

        try {
            regionBackgroundColor = typedArray.getColor(R.styleable.MapView_regionBackgroundColor, Color.BLACK)
            regionActiveColor = typedArray.getColor(
                R.styleable.MapView_regionActiveBackgroundColor,
                Color.CYAN
            )
            regionStrokeColor = typedArray.getColor(R.styleable.MapView_regionStrokeColor, Color.WHITE)
            isSelectByClickEnabled = typedArray.getBoolean(R.styleable.MapView_isSelectByClickEnabled, true)
            isMultiSelectEnabled = typedArray.getBoolean(R.styleable.MapView_isMultiSelectEnabled, false)
            animationDuration = typedArray.getInt(R.styleable.MapView_animationDuration, 200).toLong()
            mapAdjustViewBound = typedArray.getBoolean(R.styleable.MapView_adjustViewBound, false)
            isRegionToggleAnimated = typedArray.getBoolean(R.styleable.MapView_isRegionToggleAnimated, true)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        setMapColors()
    }

    private fun setMapColors() {
        mapPath.findAllRichPaths().forEach {
            it.fillColor = getRegionBackgroundColor(it)
            it.strokeColor = regionStrokeColor
        }
    }

    private fun getRegionFromRichPath(richPath: RichPath): Region? {
        return regions.firstOrNull { it.xmlName == richPath.name }
    }

    private fun getRegionBackgroundColor(richPath: RichPath): Int {
        val region = getRegionFromRichPath(richPath)
        return region?.backgroundColor?.let { Color.parseColor(it) } ?: regionBackgroundColor
    }

    private fun getRegionActiveColor(richPath: RichPath): Int {
        val region = getRegionFromRichPath(richPath)
        return region?.activeColor?.let { Color.parseColor(it) } ?: regionActiveColor
    }

    private fun deactivateRegion(regionPath: RichPath, isAnimated: Boolean = false) {
        RichPathAnimator.animate(regionPath)
            .interpolator(AccelerateDecelerateInterpolator())
            .duration(if (isAnimated) animationDuration else 0)
            .scale(1.05f, 1f)
            .fillColor(getRegionBackgroundColor(regionPath))
            .strokeColor(regionStrokeColor)
            .start()
        selectedRegions.remove(selectedRegions.first { it.xmlName == regionPath.name })
        drawMarkers()
    }

    fun deactivateRegion(region: Region, isToggleAnimated: Boolean = false) {
        val regionPath = mapPath.findRichPathByName(region.xmlName)
        regionPath?.let {
            deactivateRegion(regionPath)
        }
    }

    fun activateRegion(region: Region) {
        val regionPath = mapPath.findRichPathByName(region.xmlName)
        regionPath?.let {
            activateRegion(regionPath)
        }
    }

    private fun toggleRegion(regionPath: RichPath): Boolean {
        if (!isMultiSelectEnabled) {
            selectedRegions.filter { it.xmlName != regionPath.name }.forEach {
                deactivateRegion(it)
            }
        }
        return if (selectedRegions.any { it.xmlName == regionPath.name }) {
            deactivateRegion(regionPath, isRegionToggleAnimated)
            false
        } else {
            activateRegion(regionPath)
            true
        }
    }

    private fun activateRegion(richPath: RichPath) {
        RichPathAnimator.animate(richPath)
            .interpolator(AccelerateDecelerateInterpolator())
            .duration(if (isRegionToggleAnimated) animationDuration else 0)
            .scale(1.05f, 1f)
            .fillColor(
                richPath.fillColor,
                getRegionActiveColor(richPath)
            )
            .strokeColor(richPath.strokeColor, regionStrokeColor)
            .start()
        selectedRegions.add(regions.first { it.xmlName == richPath.name })
        drawMarkers()
    }

    private fun drawMarkers() {
        markerList.forEach {
            removeView(it)
        }
        selectedRegions.forEach {
            val regionPath = mapPath.findRichPathByName(it.xmlName)
            val regionBounds = RectF()
            regionPath?.computeBounds(regionBounds, true)
            val markerView = getMarkerView()
            markerView?.let { marker ->
                marker.bindView(it)
                addView(marker)
                marker.measure(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                val width: Int = marker.measuredWidth
                val height: Int = marker.measuredHeight
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Log.d("translation", "${regionBounds.centerX()}, ${this.height}, $height")
                    marker.translationY = min(regionBounds.centerY(), this.height.toFloat() - height - 20)
                    marker.translationX = min(regionBounds.centerX(), this.width.toFloat() - width - 20)
                }
                markerList += marker
            }
        }

    }

}