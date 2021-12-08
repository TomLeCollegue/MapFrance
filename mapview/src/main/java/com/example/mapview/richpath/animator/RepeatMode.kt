package com.example.mapview.richpath.animator

sealed class RepeatMode(val value: Int) {
    object None : RepeatMode(-2)
    object Restart : RepeatMode(1)
}
