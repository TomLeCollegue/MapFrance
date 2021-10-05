package com.test.mapfrance

import android.content.Context
import com.test.mapfrance.databinding.PoliticMarkerviewLayoutBinding

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