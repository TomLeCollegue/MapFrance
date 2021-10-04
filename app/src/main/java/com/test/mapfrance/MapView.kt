package com.test.mapfrance

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
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

    @DrawableRes
    var mapDrawable: Int = R.drawable.france


    var isSelectByClickEnabled: Boolean = true
        set(value) {
            field = value
            if (value) {
                mapPath.setOnPathClickListener {
                    activateRegion(it, withAnimate = true)
                }
            } else {
                mapPath.setOnPathClickListener(null)
            }
        }

    var isMultiSelectEnabled: Boolean = false

    var animationDuration: Long = 200L

    var regions: List<Region> = FrenchRegion.frenchRegion
    val selectedRegions: MutableList<Region> = mutableListOf()

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
        mapPath.setVectorDrawable(R.drawable.france)
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
            it.fillColor = regionBackgroundColor
            it.strokeColor = regionStrokeColor
        }
    }

    private fun deactivateRegion(regionPath: RichPath?, withAnimate: Boolean = false) {
        regionPath?.let {
            RichPathAnimator.animate(it)
                .interpolator(AccelerateDecelerateInterpolator())
                .duration(if (withAnimate) animationDuration else 0)
                .scale(1.1f, 1f)
                .fillColor(regionBackgroundColor)
                .strokeColor(regionStrokeColor)
                .start()
            selectedRegions.remove(selectedRegions.first { (xmlName) ->
                xmlName == it.name
            })
        } ?: kotlin.run {
            throw EnumConstantNotPresentException(Regions::class.java, "Province not found.")
        }
    }

    private fun deactivateRegion(region: Region) {
        val regionPath = mapPath.findRichPathByName(region.xmlName)
        deactivateRegion(regionPath, true)
    }

    private fun activateRegion(
        regionPath: RichPath?,
        withBackgroundColor: Int? = null,
        withStrokeColor: Int? = null,
        withAnimate: Boolean = false
    ) {

        //Deactivate selected provinces in single select mode
        if (!isMultiSelectEnabled) {
            regions.filter { it.xmlName != regionPath?.name }.forEach {
                deactivateRegion(it)
            }
        }

        regionPath?.let { richPath ->
            //If province is active now, deactivate it
            if (selectedRegions.any { it.xmlName == richPath.name }) {
                deactivateRegion(richPath, withAnimate)
            } else { //Activate province
                RichPathAnimator.animate(richPath)
                    .interpolator(AccelerateDecelerateInterpolator())
                    .duration(if (withAnimate) animationDuration else 0)
                    .scale(1.1f, 1f)
                    .fillColor(richPath.fillColor, withBackgroundColor ?: regionActiveColor)
                    .strokeColor(richPath.strokeColor, withStrokeColor ?: regionStrokeColor)
                    .start()
                selectedRegions.add(regions.first { it.xmlName == richPath.name })
            }
        }
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