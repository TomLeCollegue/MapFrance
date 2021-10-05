package com.test.mapfrance

object FrenchRegion {

    val frenchRegion = listOf<Region>(
        Region("GrandEst", "Grand Est"),
        Region("NouvelleAquitaine", "Nouvelle-Aquitaine"),
        Region("AuvergneRhoneAlpes", "Auvergne-Rhône-Alpes"),
        Region("BourgogneFrancheComte", "Bourgogne-Franche-Comté"),
        Region("Bretagne", "Bretagne", "#6200EE", "#018786"),
        Region("CentreValdeLoire", "Centre-Val de Loire", "#6200EE", "#018786"),
        Region("IleDeFrance", "Île-de-France", "#6200EE", "#018786"),
        Region("Occitanie", "Occitanie"),
        Region("HautDeFrance", "Hauts-de-France"),
        Region("Normandie", "Normandie"),
        Region("PaysDeLaLoire", "Pays de la Loire"),
        Region("ProvenceAlpesCoteDAzur", "Provence-Alpes-Côte d'Azur"),
        Region("Corse", "Corse")
    )

    val politicFrenchRegion = listOf<RegionWithPolitic>(
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
}