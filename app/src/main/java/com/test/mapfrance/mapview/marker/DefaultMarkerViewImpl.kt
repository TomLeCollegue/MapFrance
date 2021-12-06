package com.test.mapfrance.mapview.marker

import android.content.Context
import com.test.mapfrance.R
import com.test.mapfrance.mapview.Region
import com.test.mapfrance.databinding.MarkerviewLayoutBinding

class DefaultMarkerViewImpl(context: Context) : MarkerView(context) {

    override val layoutRes: Int
        get() = R.layout.markerview_layout

    override fun bindView(region: Region) {
        val binding = MarkerviewLayoutBinding.bind(this.getChildAt(0))
        binding.regionNameTextView.text = region.name
    }
}