package com.bignerdranch.android.nudge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.bignerdranch.android.nudge.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding   //view binding variable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)  //prevents app from going into night mode

        setUpTabBar()
    }

    //method to set up the nav bar and what is displayed
    private fun setUpTabBar() {
        binding.bottomNavBar.setOnItemSelectedListener {
            when (it) {
                R.id.nav_view -> binding.textMain.text = "Calendar View"
                R.id.nav_edit -> binding.textMain.text = "Calendar Edit"
                R.id.nav_ht -> {
                    binding.textMain.text = "Habit Tracker"
                    binding.bottomNavBar.showBadge(R.id.nav_settings)
                }
                R.id.nav_settings -> {
                    binding.textMain.text = "Settings"
                    binding.bottomNavBar.dismissBadge(R.id.nav_settings)
                }
            }
        }
    }
}