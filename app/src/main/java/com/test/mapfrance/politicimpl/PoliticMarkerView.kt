package com.test.mapfrance.politicimpl

import android.content.Context
import com.test.mapfrance.R
import com.test.mapfrance.databinding.PoliticMarkerviewLayoutBinding
import com.test.mapfrance.mapview.Region
import com.test.mapfrance.mapview.marker.MarkerView

class PoliticMarkerView(context: Context) : MarkerView(context) {

    override val layoutRes: Int
        get() = R.layout.politic_markerview_layout

    override fun bindView(region: Region) {
        val regionWithPolitic = region as RegionWithPolitic
        val binding = PoliticMarkerviewLayoutBinding.bind(this.getChildAt(0))
        binding.regionNameTextView.text = region.name
        binding.politicRegionTextView.text = regionWithPolitic.politicBorder.orientationName
    }
}