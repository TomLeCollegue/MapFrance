package com.test.mapfrance

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.mapfrance.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "France: Tendances Politiques"

        binding.map.onClickRegion = { activated, region ->
            if (activated) {
                binding.nameRegionTextView.text = region?.name
                binding.oriantationRegionTextView.text = (region as? RegionWithPolitic)?.politicBorder?.orientationName
            } else {
                binding.nameRegionTextView.text = null
                binding.oriantationRegionTextView.text = null
            }
        }
        binding.map.regions = FrenchRegion.politicFrenchRegion
        binding.map.getMarkerView = { PoliticMarkerView(this) }

    }
}