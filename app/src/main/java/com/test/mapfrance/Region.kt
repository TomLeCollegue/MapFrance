package com.test.mapfrance

open class Region(
    val xmlName: String,
    val name: String,
    val backgroundColor: String? = null,
    val activeColor: String? = null
) {
    override fun toString(): String {
        return "Region(xmlName='$xmlName', name='$name', backgroundColor=$backgroundColor, activeColor=$activeColor)"
    }
}