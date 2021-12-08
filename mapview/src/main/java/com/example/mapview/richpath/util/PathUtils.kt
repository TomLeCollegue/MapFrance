package com.example.mapview.richpath.util

import android.graphics.Path
import android.graphics.RectF
import android.graphics.Region
import com.example.mapview.richpath.pathparser.PathDataNode

object PathUtils {

    fun getPathWidth(path: Path): Float {
        val rect = RectF()
        path.computeBounds(rect, true)
        return rect.width()
    }

    fun getPathHeight(path: Path): Float {
        val rect = RectF()
        path.computeBounds(rect, true)
        return rect.height()
    }

    fun setPathDataNodes(path: Path, pathDataNodes: Array<PathDataNode>) {
        path.reset()
        PathDataNode.nodesToPath(pathDataNodes, path)
    }

    fun isTouched(path: Path, x: Float, y: Float): Boolean {
        val rectF = RectF()
        path.computeBounds(rectF, true)
        val region = Region().apply {
            setPath(
                path,
                Region(
                    rectF.left.toInt(), rectF.top.toInt(),
                    rectF.right.toInt(), rectF.bottom.toInt()
                )
            )
        }
        val offset = 10
        return (region.contains(x.toInt(), y.toInt())
                || region.contains(x.toInt() + offset, y.toInt() + offset)
                || region.contains(x.toInt() + offset, y.toInt() - offset)
                || region.contains(x.toInt() - offset, y.toInt() - offset)
                || region.contains(x.toInt() - offset, y.toInt() + offset))
    }
}