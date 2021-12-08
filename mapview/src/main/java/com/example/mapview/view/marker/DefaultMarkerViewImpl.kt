package com.example.mapview.view.marker

import android.content.Context
import com.example.mapview.R
import com.example.mapview.view.Region

class DefaultMarkerViewImpl(context: Context) : MarkerView(context) {

    override val layoutRes: Int
        get() = R.layout.markerview_layout

    override fun bindView(region: Region) {
    }
}
