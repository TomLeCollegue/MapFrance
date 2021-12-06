package com.test.mapfrance

import com.test.mapfrance.keyfigureimpl.KeyFigureRegion
import com.test.mapfrance.keyfigureimpl.parseJsonKeyFigure
import com.test.mapfrance.politicimpl.Orientation
import com.test.mapfrance.politicimpl.RegionWithPolitic

object FrenchRegion {

    val politicFrenchRegion: List<RegionWithPolitic> = listOf<RegionWithPolitic>(
        RegionWithPolitic("GrandEst", "Grand Est", Orientation.DROITE),
        RegionWithPolitic("NouvelleAquitaine", "Nouvelle-Aquitaine", Orientation.GAUCHE),
        RegionWithPolitic("AuvergneRhoneAlpes", "Auvergne-Rhône-Alpes", Orientation.DROITE),
        RegionWithPolitic("BourgogneFrancheComte", "Bourgogne-Franche-Comté", Orientation.GAUCHE),
        RegionWithPolitic("Bretagne", "Bretagne", Orientation.GAUCHE),
        RegionWithPolitic("CentreValdeLoire", "Centre-Val de Loire", Orientation.GAUCHE),
        RegionWithPolitic("IleDeFrance", "Île-de-France", Orientation.DROITE),
        RegionWithPolitic("Occitanie", "Occitanie", Orientation.GAUCHE),
        RegionWithPolitic("HautDeFrance", "Hauts-de-France", Orientation.DROITE),
        RegionWithPolitic("Normandie", "Normandie", Orientation.DROITE),
        RegionWithPolitic("PaysDeLaLoire", "Pays de la Loire", Orientation.DROITE),
        RegionWithPolitic("ProvenceAlpesCoteDAzur", "Provence-Alpes-Côte d'Azur", Orientation.DROITE),
        RegionWithPolitic("Corse", "Corse", Orientation.GAUCHE)
    )

    val vaccinationFrenchRegion: List<KeyFigureRegion> = parseJsonKeyFigure()
}