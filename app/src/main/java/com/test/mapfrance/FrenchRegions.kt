package com.test.mapfrance

import com.test.mapfrance.keyfigureimpl.KeyFigureRegion
import com.test.mapfrance.keyfigureimpl.parseJsonKeyFigure

object FrenchRegion {
    val vaccinationFrenchRegion: List<KeyFigureRegion> = parseJsonKeyFigure()
}