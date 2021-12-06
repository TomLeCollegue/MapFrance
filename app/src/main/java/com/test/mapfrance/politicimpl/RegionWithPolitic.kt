package com.test.mapfrance.politicimpl

import com.test.mapfrance.mapview.Region

enum class Orientation(val orientationName: String, val backGroundColor: String, val colorSelected: String) {
    GAUCHE("Gauche", "#E3242B", "#4E0707"),
    DROITE("Droite", "#1167B1", "#03254C")
}

data class RegionWithPolitic(
    override var xmlName: String,
    override var name: String,
    val politicBorder: Orientation,
    override var backgroundColor: String? = politicBorder.backGroundColor,
    override var colorSelected: String? = politicBorder.colorSelected
) : Region()