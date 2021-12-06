package com.richpath.model

import android.content.Context
import android.content.res.XmlResourceParser
import com.test.mapfrance.richpath.RichPath
import com.test.mapfrance.richpath.util.XmlParser

class Vector {
    companion object {
        const val TAG_NAME = "vector"
    }

    var name: String? = null
    private var tint = 0
    var height: Float = 0f
        private set
    var width: Float = 0f
        private set
    private var alpha = 0f

    private var autoMirrored = false

    //TODO private PorterDuff.Mode tintMode = PorterDuff.Mode.SRC_IN;

    var viewportWidth: Float = 0f
        private set
    var viewportHeight: Float = 0f
        private set
    var currentWidth: Float = 0f
    var currentHeight: Float = 0f

    var paths: ArrayList<RichPath> = ArrayList()

    fun inflate(xpp: XmlResourceParser, context: Context) {
        name = XmlParser.getAttributeString(context, xpp, "name", name)
        tint = XmlParser.getAttributeColor(context, xpp, "tint", tint)
        width = XmlParser.getAttributeDimen(context, xpp, "width", width)
        height = XmlParser.getAttributeDimen(context, xpp, "height", height)
        alpha = XmlParser.getAttributeFloat(xpp, "alpha", alpha)
        autoMirrored = XmlParser.getAttributeBoolean(xpp, "autoMirrored", autoMirrored)
        viewportWidth = XmlParser.getAttributeFloat(xpp, "viewportWidth", viewportWidth)
        viewportHeight = XmlParser.getAttributeFloat(xpp, "viewportHeight", viewportHeight)
        currentWidth = viewportWidth
        currentHeight = viewportHeight
        //TODO tintMode = XmlParser.getAttributeFloat(xpp, "tintMode", tintMode);
    }
}