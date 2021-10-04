package com.test.mapfrance

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.FrameLayout
import androidx.annotation.DrawableRes
import com.richpath.RichPath
import com.richpath.RichPathView
import com.richpathanimator.RichPathAnimator

class MapView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private lateinit var surfaceView: SurfaceView
    private lateinit var mapPath: RichPathView

    var regionBackgroundColor: Int = Color.BLACK

    var regionActiveColor: Int = Color.CYAN

    var regionStrokeColor: Int = Color.WHITE

    var mapAdjustViewBound: Boolean = false

    var onClickRegion: (Boolean, Region?) -> Unit = { _, _ -> }

    @DrawableRes
    var mapDrawable: Int = R.drawable.france
        set(value) {
            field = value
            removeAllViews()
            addPathView()
            addSurfaceView()
            initMap()
            isSelectByClickEnabled = isSelectByClickEnabled
        }


    var isSelectByClickEnabled: Boolean = true
        set(value) {
            field = value
            if (value) {
                mapPath.setOnPathClickListener {
                    onClickRegion(
                        toggleRegion(it, isAnimated = true),
                        getRegionFromRichPath(it)
                    )
                }
            } else {
                mapPath.setOnPathClickListener(null)
            }
        }

    var isMultiSelectEnabled: Boolean = false

    var animationDuration: Long = 200L

    var regions: List<Region> = FrenchRegion.frenchRegion
    private val selectedRegions: MutableList<Region> = mutableListOf()

    init {
        addPathView()
        addSurfaceView()
        handleAttr(context, attrs)
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

    private fun addSurfaceView() {
        //Add surface view for draw titles on map
        surfaceView = SurfaceView(context)
        surfaceView.layoutParams = LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT
        )
        addView(surfaceView)
    }

    private fun handleAttr(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.MapView, 0, 0)

        try {
            regionBackgroundColor =
                typedArray.getColor(R.styleable.MapView_RegionBackgroundColor, Color.BLACK)
            regionActiveColor = typedArray.getColor(
                R.styleable.MapView_RegionActiveBackgroundColor,
                Color.CYAN
            )
            regionStrokeColor =
                typedArray.getColor(R.styleable.MapView_RegionStrokeColor, Color.WHITE)
            isSelectByClickEnabled =
                typedArray.getBoolean(R.styleable.MapView_RegionSelectByClick, true)
            isMultiSelectEnabled =
                typedArray.getBoolean(R.styleable.MapView_RegionMultiSelect, false)
            animationDuration =
                typedArray.getInt(R.styleable.MapView_AnimationDuration, 200).toLong()
            mapAdjustViewBound = typedArray.getBoolean(R.styleable.MapView_AdjustViewBound, false)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            typedArray.recycle()
        }
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        initMap()
    }

    private fun initMap() {
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

    private fun deactivateRegion(regionPath: RichPath?, isAnimated: Boolean = false) {
        regionPath?.let {
            RichPathAnimator.animate(it)
                .interpolator(AccelerateDecelerateInterpolator())
                .duration(if (isAnimated) animationDuration else 0)
                .scale(1.05f, 1f)
                .fillColor(getRegionBackgroundColor(it))
                .strokeColor(regionStrokeColor)
                .start()
            selectedRegions.remove(selectedRegions.first { region ->
                region.xmlName == it.name
            })
        }
    }

    private fun deactivateRegion(region: Region) {
        val regionPath = mapPath.findRichPathByName(region.xmlName)
        deactivateRegion(regionPath)
    }

    private fun toggleRegion(
        regionPath: RichPath,
        isAnimated: Boolean = false
    ): Boolean {

        if (!isMultiSelectEnabled) {
            selectedRegions.filter { it.xmlName != regionPath.name }.forEach {
                deactivateRegion(it)
            }
        }
        return if (selectedRegions.any { it.xmlName == regionPath.name }) {
            deactivateRegion(regionPath, isAnimated)
            false
        } else {
            activateRegion(regionPath, true)
            true
        }
    }


    private fun activateRegion(richPath: RichPath, isAnimated: Boolean = false) {
        RichPathAnimator.animate(richPath)
            .interpolator(AccelerateDecelerateInterpolator())
            .duration(if (isAnimated) animationDuration else 0)
            .scale(1.05f, 1f)
            .fillColor(
                richPath.fillColor,
                getRegionActiveColor(richPath)
            )
            .strokeColor(richPath.strokeColor, regionStrokeColor)
            .start()
        selectedRegions.add(regions.first { it.xmlName == richPath.name })
    }


    inner class SurfaceView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
    ) : View(context, attrs, defStyleAttr) {
        init {
            //Force layout to call onDraw method
            setWillNotDraw(false)
        }

        @SuppressLint("DrawAllocation")
        override fun onDraw(canvas: Canvas?) {
            super.onDraw(canvas)
            /* titles.forEach {
                val provinceBounds = RectF()
                val textBounds = Rect()
                //Find title bounds
                paint.getTextBounds(it.value ?: "", 0, it.value?.length ?: 0, textBounds)
                //Find province bounds
                it.key?.computeBounds(provinceBounds, true)
                //Draw text on center of province
                canvas?.drawText(
                    it.value ?: "",
                    provinceBounds.centerX().minus(textBounds.width().div(2)),
                    provinceBounds.centerY(),
                    paint
                )

            } */
        }
    }

}