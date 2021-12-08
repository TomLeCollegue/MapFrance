package com.example.mapview.richpath

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.MotionEvent
import android.widget.ImageView.ScaleType
import androidx.annotation.IntRange
import com.example.mapview.richpath.listener.OnRichPathUpdatedListener
import com.example.mapview.richpath.model.Vector
import com.example.mapview.richpath.pathparser.PathParser
import com.example.mapview.richpath.util.PathUtils
import kotlin.math.min

class RichPathDrawable(
    private val vector: Vector?, private val scaleType: ScaleType
) : Drawable() {

    private var width: Int = 0
    private var height: Int = 0

    init {
        listenToPathsUpdates()
    }

    override fun onBoundsChange(bounds: Rect?) {
        super.onBoundsChange(bounds)
        bounds?.let {
            if (it.width() > 0 && it.height() > 0) {
                width = it.width()
                height = it.height()
                mapPaths()
            }
        }
    }

    private fun mapPaths() {
        val vector = vector ?: return

        val centerX = width / 2f
        val centerY = height / 2f

        val matrix = Matrix()

        matrix.postTranslate(
            centerX - vector.currentWidth / 2,
            centerY - vector.currentHeight / 2
        )

        val widthRatio = width / vector.currentWidth
        val heightRatio = height / vector.currentHeight


        if (scaleType == ScaleType.FIT_XY) {
            matrix.postScale(widthRatio, heightRatio, centerX, centerY)
        } else {
            val ratio: Float = if (width < height) {
                widthRatio
            } else {
                heightRatio
            }
            matrix.postScale(ratio, ratio, centerX, centerY)
        }

        val absWidthRatio = width / vector.viewportWidth
        val absHeightRatio = height / vector.viewportHeight
        val absRatio = min(absWidthRatio, absHeightRatio)

        for (path in vector.paths) {
            path.mapToMatrix(matrix)
            path.scaleStrokeWidth(absRatio)
        }

        vector.currentWidth = width.toFloat()
        vector.currentHeight = height.toFloat()
    }

    fun findAllRichPaths(): Array<RichPath> {
        return vector?.paths?.toTypedArray() ?: arrayOf()
    }

    fun findRichPathByName(name: String): RichPath? {
        val vector = vector ?: return null
        for (path in vector.paths) {
            if (name == path.name) {
                return path
            }
        }
        return null
    }

    private fun listenToPathsUpdates() {
        val vector = vector ?: return
        for (path in vector.paths) {
            path.onRichPathUpdatedListener = object : OnRichPathUpdatedListener {
                override fun onPathUpdated() {
                    invalidateSelf()
                }
            }
        }
    }

    fun getTouchedPath(event: MotionEvent?): RichPath? {
        val vector = vector ?: return null

        when (event?.action) {
            MotionEvent.ACTION_UP -> {
                for (i in vector.paths.indices.reversed()) {
                    val richPath = vector.paths[i]
                    if (PathUtils.isTouched(richPath, event.x, event.y)) {
                        return richPath
                    }
                }
            }
        }

        return null
    }

    override fun draw(canvas: Canvas) {
        if (vector == null || vector.paths.size < 0) return

        for (path in vector.paths) {
            path.draw(canvas)
        }
    }

    override fun setAlpha(@IntRange(from = 0, to = 255) alpha: Int) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }
}