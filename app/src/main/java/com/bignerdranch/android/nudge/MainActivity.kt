package com.bignerdranch.android.nudge

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.bignerdranch.android.nudge.databinding.ActivityMainBinding
import com.bignerdranch.android.nudge.fragments.CalendarEditFragment
import com.bignerdranch.android.nudge.fragments.CalendarViewFragment
import com.bignerdranch.android.nudge.fragments.HabitFragment
import com.bignerdranch.android.nudge.fragments.SettingsFragment

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
                R.id.nav_view -> {
                    binding.textMain.text = ""
                    makeCurrentFragment(CalendarViewFragment())
                }
                R.id.nav_edit -> {
                    binding.textMain.text = ""
                    makeCurrentFragment(CalendarEditFragment())
                }
                R.id.nav_ht -> {
                    binding.textMain.text = ""
                    makeCurrentFragment(HabitFragment())
                    binding.bottomNavBar.showBadge(R.id.nav_settings)
                }
                R.id.nav_settings -> {
                    binding.textMain.text = ""
                    makeCurrentFragment(SettingsFragment())
                    binding.bottomNavBar.dismissBadge(R.id.nav_settings)
                }
            }
        }
    }

    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fl_wrapper, fragment)
            commit()
        }
}
