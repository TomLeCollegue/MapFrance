package com.test.mapfrance.keyfigureimpl

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import com.test.mapfrance.mapview.Region
import java.util.Random

data class KeyFigureRegion(
    override var xmlName: String,
    override var name: String,
    var entries: List<Serie>
) : Region() {

    override var backgroundColor: String? = null
        get() = calculateColor()

    fun calculateColor(): String {
        val first = entries.last().value - entries[entrieSelected.coerceIn(0, entries.size - 1)].value
        val second = entries.last().value - entries[0].value

        val pourcent = (Random().nextInt(100).toFloat() / 100) * 255F
        /*val pourcent = entries[entrieSelected.coerceIn(
            0,
            entries.size - 1
        )].value.toFloat() / getNumberInhabittant(xmlName).toFloat() * 255F
        */
        val hex = "0${Integer.toHexString(pourcent.toInt())}".takeLast(2)
        return "#$hex$colorRGB"
    }

    companion object {
        const val franceinhabitants: Int = 67390000 / 20
        const val colorRGB: String = "E17178"
    }

    fun getNumberInhabittant(region: String): Int {
        return when (region) {
            "AuvergneRhoneAlpes" -> 8092598
            "HautDeFrance" -> 5977462
            "ProvenceAlpesCoteDAzur" -> 5089661
            "GrandEst" -> 5524817
            "Occitanie" -> 5524817
            "Normandie" -> 5524817
            "NouvelleAquitaine" -> 5524817
            "CentreValdeLoire" -> 5524817
            "BourgogneFrancheComte" -> 5524817
            "Bretagne" -> 5524817
            "Corse" -> 5524817
            "PaysDeLaLoire" -> 5524817
            "IleDeFrance" -> 5524817
            else -> 0
        }
    }
}

fun parseJsonKeyFigure(): List<KeyFigureRegion> {
    val gson = Gson()
    return (gson.fromJson(jsonKeyFigure, object : TypeToken<List<Departement>>() {}.type) as List<Departement>).let {
        it.map { map ->
            KeyFigureRegion(
                map.dptNb,
                map.dptLabel,
                map.series
            )
        }
    }
}

data class Departement(
    @SerializedName("dptNb")
    val dptNb: String,

    @SerializedName("dptLabel")
    val dptLabel: String,

    @SerializedName("series")
    val series: List<Serie>
)

data class Serie(
    @SerializedName("date")
    val date: Int,

    @SerializedName("value")
    val value: Int
)

val jsonKeyFigure: String = "[\n" +
    "{\"dptNb\":\"AuvergneRhoneAlpes\",\"dptLabel\":\"Auvergne Rhône Alpes\",\"extractDate\":1638273600,\"value\":6233881,\"valueToDisplay\":\"6,23 M\",\"series\":[{\"date\":1637150400,\"value\":6202080},{\"date\":1637236800,\"value\":6204419},{\"date\":1637323200,\"value\":6208000},{\"date\":1637409600,\"value\":6210931},{\"date\":1637496000,\"value\":6211061},{\"date\":1637582400,\"value\":6213654},{\"date\":1637668800,\"value\":6216426},{\"date\":1637755200,\"value\":6220230},{\"date\":1637841600,\"value\":6222792},{\"date\":1637928000,\"value\":6226497},{\"date\":1638014400,\"value\":6229251},{\"date\":1638100800,\"value\":6229481},{\"date\":1638187200,\"value\":6231518},{\"date\":1638273600,\"value\":6233881}]},\n" +
    "{\"dptNb\":\"HautDeFrance\",\"dptLabel\":\"Hauts de France\",\"extractDate\":1638273600,\"value\":4561579,\"valueToDisplay\":\"4,56 M\",\"series\":[{\"date\":1637150400,\"value\":4536954},{\"date\":1637236800,\"value\":4538923},{\"date\":1637323200,\"value\":4541938},{\"date\":1637409600,\"value\":4544561},{\"date\":1637496000,\"value\":4544771},{\"date\":1637582400,\"value\":4546571},{\"date\":1637668800,\"value\":4548542},{\"date\":1637755200,\"value\":4550835},{\"date\":1637841600,\"value\":4553004},{\"date\":1637928000,\"value\":4555726},{\"date\":1638014400,\"value\":4558008},{\"date\":1638100800,\"value\":4558353},{\"date\":1638187200,\"value\":4559750},{\"date\":1638273600,\"value\":4561579}]},\n" +
    "{\"dptNb\":\"ProvenceAlpesCoteDAzur\",\"dptLabel\":\"Provence Alpes Côte d'Azur\",\"extractDate\":1638273600,\"value\":3896758,\"valueToDisplay\":\"3,90 M\",\"series\":[{\"date\":1637150400,\"value\":3869572},{\"date\":1637236800,\"value\":3871902},{\"date\":1637323200,\"value\":3874769},{\"date\":1637409600,\"value\":3876756},{\"date\":1637496000,\"value\":3876955},{\"date\":1637582400,\"value\":3879117},{\"date\":1637668800,\"value\":3881400},{\"date\":1637755200,\"value\":3884410},{\"date\":1637841600,\"value\":3887045},{\"date\":1637928000,\"value\":3889987},{\"date\":1638014400,\"value\":3892042},{\"date\":1638100800,\"value\":3892256},{\"date\":1638187200,\"value\":3894355},{\"date\":1638273600,\"value\":3896758}]},\n" +
    "{\"dptNb\":\"GrandEst\",\"dptLabel\":\"Grand Est\",\"extractDate\":1638273600,\"value\":4211002,\"valueToDisplay\":\"4,21 M\",\"series\":[{\"date\":1637150400,\"value\":4187389},{\"date\":1637236800,\"value\":4188940},{\"date\":1637323200,\"value\":4191645},{\"date\":1637409600,\"value\":4193633},{\"date\":1637496000,\"value\":4193846},{\"date\":1637582400,\"value\":4195753},{\"date\":1637668800,\"value\":4197921},{\"date\":1637755200,\"value\":4200381},{\"date\":1637841600,\"value\":4202221},{\"date\":1637928000,\"value\":4205139},{\"date\":1638014400,\"value\":4207237},{\"date\":1638100800,\"value\":4207496},{\"date\":1638187200,\"value\":4209121},{\"date\":1638273600,\"value\":4211002}]},\n" +
    "{\"dptNb\":\"Occitanie\",\"dptLabel\":\"Occitanie\",\"extractDate\":1638273600,\"value\":4562942,\"valueToDisplay\":\"4,56 M\",\"series\":[{\"date\":1637150400,\"value\":4537661},{\"date\":1637236800,\"value\":4539740},{\"date\":1637323200,\"value\":4542634},{\"date\":1637409600,\"value\":4544382},{\"date\":1637496000,\"value\":4544638},{\"date\":1637582400,\"value\":4546436},{\"date\":1637668800,\"value\":4548762},{\"date\":1637755200,\"value\":4551379},{\"date\":1637841600,\"value\":4553776},{\"date\":1637928000,\"value\":4556893},{\"date\":1638014400,\"value\":4558723},{\"date\":1638100800,\"value\":4559029},{\"date\":1638187200,\"value\":4560582},{\"date\":1638273600,\"value\":4562942}]},\n" +
    "{\"dptNb\":\"Normandie\",\"dptLabel\":\"Normandie\",\"extractDate\":1638273600,\"value\":2729435,\"valueToDisplay\":\"2,73 M\",\"series\":[{\"date\":1637150400,\"value\":2717133},{\"date\":1637236800,\"value\":2717971},{\"date\":1637323200,\"value\":2719485},{\"date\":1637409600,\"value\":2720723},{\"date\":1637496000,\"value\":2720763},{\"date\":1637582400,\"value\":2721669},{\"date\":1637668800,\"value\":2722832},{\"date\":1637755200,\"value\":2724342},{\"date\":1637841600,\"value\":2725236},{\"date\":1637928000,\"value\":2726553},{\"date\":1638014400,\"value\":2727538},{\"date\":1638100800,\"value\":2727674},{\"date\":1638187200,\"value\":2728401},{\"date\":1638273600,\"value\":2729435}]},\n" +
    "{\"dptNb\":\"NouvelleAquitaine\",\"dptLabel\":\"Nouvelle Aquitaine\",\"extractDate\":1638273600,\"value\":4918757,\"valueToDisplay\":\"4,92 M\",\"series\":[{\"date\":1637150400,\"value\":4895702},{\"date\":1637236800,\"value\":4897585},{\"date\":1637323200,\"value\":4900185},{\"date\":1637409600,\"value\":4901854},{\"date\":1637496000,\"value\":4901939},{\"date\":1637582400,\"value\":4903670},{\"date\":1637668800,\"value\":4905972},{\"date\":1637755200,\"value\":4908460},{\"date\":1637841600,\"value\":4910698},{\"date\":1637928000,\"value\":4913315},{\"date\":1638014400,\"value\":4914922},{\"date\":1638100800,\"value\":4915102},{\"date\":1638187200,\"value\":4916632},{\"date\":1638273600,\"value\":4918757}]},\n" +
    "{\"dptNb\":\"CentreValdeLoire\",\"dptLabel\":\"Centre Val de Loire\",\"extractDate\":1638273600,\"value\":1977055,\"valueToDisplay\":\"1,98 M\",\"series\":[{\"date\":1637150400,\"value\":1967858},{\"date\":1637236800,\"value\":1968607},{\"date\":1637323200,\"value\":1969749},{\"date\":1637409600,\"value\":1970471},{\"date\":1637496000,\"value\":1970552},{\"date\":1637582400,\"value\":1971291},{\"date\":1637668800,\"value\":1972139},{\"date\":1637755200,\"value\":1973278},{\"date\":1637841600,\"value\":1974009},{\"date\":1637928000,\"value\":1975132},{\"date\":1638014400,\"value\":1975777},{\"date\":1638100800,\"value\":1975819},{\"date\":1638187200,\"value\":1976393},{\"date\":1638273600,\"value\":1977055}]},\n" +
    "{\"dptNb\":\"BourgogneFrancheComte\",\"dptLabel\":\"Bourgogne Franche Comté\",\"extractDate\":1638273600,\"value\":2163732,\"valueToDisplay\":\"2,16 M\",\"series\":[{\"date\":1637150400,\"value\":2152468},{\"date\":1637236800,\"value\":2153194},{\"date\":1637323200,\"value\":2154670},{\"date\":1637409600,\"value\":2155550},{\"date\":1637496000,\"value\":2155594},{\"date\":1637582400,\"value\":2156545},{\"date\":1637668800,\"value\":2157373},{\"date\":1637755200,\"value\":2158819},{\"date\":1637841600,\"value\":2159632},{\"date\":1637928000,\"value\":2161148},{\"date\":1638014400,\"value\":2161923},{\"date\":1638100800,\"value\":2161958},{\"date\":1638187200,\"value\":2162765},{\"date\":1638273600,\"value\":2163732}]},\n" +
    "{\"dptNb\":\"Bretagne\",\"dptLabel\":\"Bretagne\",\"extractDate\":1638273600,\"value\":2783029,\"valueToDisplay\":\"2,78 M\",\"series\":[{\"date\":1637150400,\"value\":2770536},{\"date\":1637236800,\"value\":2771660},{\"date\":1637323200,\"value\":2773198},{\"date\":1637409600,\"value\":2774157},{\"date\":1637496000,\"value\":2774194},{\"date\":1637582400,\"value\":2775275},{\"date\":1637668800,\"value\":2776386},{\"date\":1637755200,\"value\":2777739},{\"date\":1637841600,\"value\":2778895},{\"date\":1637928000,\"value\":2780415},{\"date\":1638014400,\"value\":2781196},{\"date\":1638100800,\"value\":2781228},{\"date\":1638187200,\"value\":2782095},{\"date\":1638273600,\"value\":2783029}]},\n" +
    "{\"dptNb\":\"Corse\",\"dptLabel\":\"Corse\",\"extractDate\":1638273600,\"value\":253800,\"valueToDisplay\":\"253800\",\"series\":[{\"date\":1637150400,\"value\":251730},{\"date\":1637236800,\"value\":251882},{\"date\":1637323200,\"value\":252058},{\"date\":1637409600,\"value\":252230},{\"date\":1637496000,\"value\":252329},{\"date\":1637582400,\"value\":252481},{\"date\":1637668800,\"value\":252623},{\"date\":1637755200,\"value\":252811},{\"date\":1637841600,\"value\":252971},{\"date\":1637928000,\"value\":253121},{\"date\":1638014400,\"value\":253362},{\"date\":1638100800,\"value\":253459},{\"date\":1638187200,\"value\":253608},{\"date\":1638273600,\"value\":253800}]},\n" +
    "{\"dptNb\":\"PaysDeLaLoire\",\"dptLabel\":\"Pays de Loire\",\"extractDate\":1638273600,\"value\":3078986,\"valueToDisplay\":\"3,08 M\",\"series\":[{\"date\":1637150400,\"value\":3064947},{\"date\":1637236800,\"value\":3066056},{\"date\":1637323200,\"value\":3067649},{\"date\":1637409600,\"value\":3068499},{\"date\":1637496000,\"value\":3068566},{\"date\":1637582400,\"value\":3069664},{\"date\":1637668800,\"value\":3071029},{\"date\":1637755200,\"value\":3072727},{\"date\":1637841600,\"value\":3073954},{\"date\":1637928000,\"value\":3075764},{\"date\":1638014400,\"value\":3076591},{\"date\":1638100800,\"value\":3076726},{\"date\":1638187200,\"value\":3077752},{\"date\":1638273600,\"value\":3078986}]},\n" +
    "{\"dptNb\":\"IleDeFrance\",\"dptLabel\":\"Ile de France\",\"extractDate\":1638273600,\"value\":9443206,\"valueToDisplay\":\"9,44 M\",\"series\":[{\"date\":1637150400,\"value\":9392268},{\"date\":1637236800,\"value\":9396255},{\"date\":1637323200,\"value\":9401644},{\"date\":1637409600,\"value\":9407313},{\"date\":1637496000,\"value\":9408206},{\"date\":1637582400,\"value\":9411922},{\"date\":1637668800,\"value\":9416189},{\"date\":1637755200,\"value\":9421244},{\"date\":1637841600,\"value\":9425522},{\"date\":1637928000,\"value\":9430490},{\"date\":1638014400,\"value\":9435189},{\"date\":1638100800,\"value\":9436411},{\"date\":1638187200,\"value\":9439364},{\"date\":1638273600,\"value\":9443206}]}\n" +
    "]"


