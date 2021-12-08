package com.example.mapview.view

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.example.mapview.R
import com.example.mapview.richpath.RichPath
import com.example.mapview.richpath.RichPathView
import com.example.mapview.richpath.animator.RichPathAnimator
import com.example.mapview.view.marker.MarkerView
import kotlinx.coroutines.ExperimentalCoroutinesApi

@OptIn(ExperimentalCoroutinesApi::class)
class MapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var mapPath: RichPathView
    var regionBackgroundColor: Int = Color.BLACK
    var isRegionToggleAnimated: Boolean = true
    var regionSelectedColor: Int = Color.CYAN
    var regionStrokeColor: Int = Color.WHITE
    var getMarkerView: () -> MarkerView? = { null }
        set(value) {
            field = value
            resetMap()
        }
    var onClickRegion: (Boolean, Region?) -> Unit = { _, _ -> }

    @DrawableRes
    var mapDrawable: Int? = null
        set(value) {
            field = value
            resetMap()
        }

    var isToggleByClickEnabled: Boolean = true
        set(value) {
            field = value
            toggleSelectByClick()
        }

    var isMultiSelectEnabled: Boolean = false

    var animationDuration: Long = 200L

    var regions: List<Region> = listOf()
        set(value) {
            field = value
            resetMap()
        }

    private val selectedRegions: MutableList<Region> = mutableListOf()
    private val markerList: MutableList<View> = mutableListOf()

    init {
        addPathView()
        handleAttr(context, attrs)
    }

    fun unselectRegion(region: Region) {
        val regionPath = mapPath.findRichPathByName(region.xmlName)
        regionPath?.let {
            regionPath.unselect()
        }
    }

    fun selectRegion(region: Region) {
        val regionPath = mapPath.findRichPathByName(region.xmlName)
        regionPath?.let {
            regionPath.selectRegion()
        }
    }

    private fun toggleSelectByClick() {
        if (isToggleByClickEnabled) {
            mapPath.setOnPatchClickListener {
                onClickRegion(
                    it.toggle(),
                    it.getRegionFromRichPath()
                )
            }
        } else {
            mapPath.onPathClickListener = null
        }
    }

    fun resetMap() {
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
        mapPath.adjustViewBounds = true
        mapDrawable?.let { mapPath.setVectorDrawable(it) }
        addView(mapPath)
    }

    private fun handleAttr(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MapView, 0, 0)
        try {
            regionBackgroundColor =
                typedArray.getColor(R.styleable.MapView_regionBackgroundColor, Color.BLACK)
            regionSelectedColor =
                typedArray.getColor(R.styleable.MapView_regionSelectedColor, Color.CYAN)
            regionStrokeColor =
                typedArray.getColor(R.styleable.MapView_regionStrokeColor, Color.WHITE)
            isToggleByClickEnabled =
                typedArray.getBoolean(R.styleable.MapView_isToggleByClickEnabled, true)
            isMultiSelectEnabled =
                typedArray.getBoolean(R.styleable.MapView_isMultiSelectEnabled, false)
            animationDuration =
                typedArray.getInt(R.styleable.MapView_animationDuration, 200).toLong()
            isRegionToggleAnimated =
                typedArray.getBoolean(R.styleable.MapView_isRegionToggleAnimated, true)
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
            it.fillColor = it.regionBackgroundColor()
            it.strokeColor = regionStrokeColor
        }
    }

    private fun RichPath.getRegionFromRichPath(): Region? {
        return regions.firstOrNull { it.xmlName == this.name }
    }

    private fun RichPath.regionBackgroundColor(): Int {
        val region = this.getRegionFromRichPath()
        return region?.backgroundColor?.let { Color.parseColor(it) } ?: regionBackgroundColor
    }

    private fun RichPath.regionSelectedColor(): Int {
        val region = this.getRegionFromRichPath()
        return region?.colorSelected?.let { Color.parseColor(it) } ?: regionSelectedColor
    }

    private fun RichPath.toggle(): Boolean {
        if (!isMultiSelectEnabled) {
            selectedRegions.filter { it.xmlName != this.name }.forEach {
                unselectRegion(it)
            }
        }
        return if (selectedRegions.any { it.xmlName == this.name }) {
            this.unselect(isRegionToggleAnimated)
            false
        } else {
            this.selectRegion()
            true
        }
    }

    private fun RichPath.unselect(isAnimated: Boolean = false) {
        RichPathAnimator.animate(this)
            .interpolator(AccelerateDecelerateInterpolator())
            .duration(if (isAnimated) animationDuration else 0)
            .scale(1.05f, 1f)
            .fillColor(this.regionBackgroundColor())
            .strokeColor(regionStrokeColor)
            .start()
        selectedRegions.remove(selectedRegions.first { it.xmlName == this.name })
        drawMarkers()
    }

    private fun RichPath.selectRegion() {
        RichPathAnimator.animate(this)
            .interpolator(AccelerateDecelerateInterpolator())
            .duration(if (isRegionToggleAnimated) animationDuration else 0)
            .scale(1.05f, 1f)
            .fillColor(
                this.fillColor,
                this.regionSelectedColor()
            )
            .strokeColor(this.strokeColor, regionStrokeColor)
            .start()
        selectedRegions.add(regions.first { it.xmlName == this.name })
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
                marker.translationY =
                    minOf(regionBounds.centerY(), this.height.toFloat() - height - 20)
                marker.translationX =
                    minOf(regionBounds.centerX(), this.width.toFloat() - width - 20)

                markerList += marker
            }
        }

    }

}