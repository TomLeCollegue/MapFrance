package com.test.mapfrance

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.test.mapfrance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "France: Vaccination"
        val colors = FrenchRegion.vaccinationFrenchRegion.map {
            it.backgroundColor
        }

        binding.buttonMinus.setOnClickListener {
            FrenchRegion.vaccinationFrenchRegion.forEach {
                it.entrieSelected -= 1
            }
            binding.map.resetMap()
        }

        binding.buttonPlus.setOnClickListener {
            FrenchRegion.vaccinationFrenchRegion.forEach {
                it.entrieSelected += 1
            }
            binding.map.resetMap()
        }

        Log.d("observe", "$colors")
        binding.map.mapDrawable = R.drawable.france
        binding.map.regions = FrenchRegion.vaccinationFrenchRegion
        //binding.map.getMarkerView = { PoliticMarkerView(this) }

    }
}