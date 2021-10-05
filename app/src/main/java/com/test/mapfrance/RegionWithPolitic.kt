package com.test.mapfrance

enum class Orientation(val orientationName: String, val color: String, val colorSelected: String) {
    GAUCHE("Gauche", "#E3242B", "#4E0707"),
    DROITE("Droite", "#1167B1", "#03254C")
}

class RegionWithPolitic(
    xmlName: String,
    name: String,
    val politicBorder: Orientation
) : Region(
    xmlName,
    name,
    politicBorder.color,
    politicBorder.colorSelected,
)