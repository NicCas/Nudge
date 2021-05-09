package com.bignerdranch.android.nudge.habittracker.ui.fragments.createhabit

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bignerdranch.android.nudge.R
import com.bignerdranch.android.nudge.habittracker.data.models.Habit
import com.bignerdranch.android.nudge.habittracker.logic.utils.Calculations
import com.bignerdranch.android.nudge.habittracker.ui.viewmodels.HabitViewModel

import kotlinx.android.synthetic.main.fragment_create_habit_item.*
import java.util.*

class CreateHabitItem : Fragment(R.layout.fragment_create_habit_item),
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private var title = ""
    private var description = ""
    private var drawableSelected = 0
    private var timeStamp = ""

    private lateinit var habitViewModel: HabitViewModel

    private var day = 0
    private var month = 0
    private var year = 0
    private var hour = 0
    private var minute = 0

    private var cleanDate = ""
    private var cleanTime = ""


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        habitViewModel = ViewModelProvider(this).get(HabitViewModel::class.java)

        backButton.setOnClickListener {
            findNavController().navigate(R.id.action_createHabitItem_to_habitList)
        }

        //Add habit to database
        btn_add_habit.setOnClickListener {
            hideKeyboard()
            addHabitToDB()
        }
        //Pick a date and time
        pickDateAndTime()

        //Selected and image to put into our list
        drawableSelected()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    //set on click listeners for our data and time pickers
    private fun pickDateAndTime() {
        btn_pickDate.setOnClickListener {
            hideKeyboard()
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        btn_pickTime.setOnClickListener {
            hideKeyboard()
            getTimeCalendar()
            TimePickerDialog(context, this, hour, minute, true).show()
        }
    }

    //Hiding the keyboard
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun addHabitToDB() {

        //Get text from editTexts
        title = et_habitName.text.toString()
        description = et_habitDescription.text.toString()

        //Create a timestamp string for our recyclerview
        timeStamp = "$cleanDate $cleanTime"

        //Check that the form is complete before submitting data to the database
        if (!(title.isEmpty() || description.isEmpty() || timeStamp.isEmpty() || drawableSelected == 0)) {
            val habit = Habit(0, title, description, timeStamp, drawableSelected)

            //add the habit if all the fields are filled
            habitViewModel.addHabit(habit)
            Toast.makeText(context, "Habit created successfully!", Toast.LENGTH_SHORT).show()

            //navigate back to our home fragment
            findNavController().navigate(R.id.action_createHabitItem_to_habitList)
        } else {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    // The selector for the icons that will appear in the recycler view
    private fun drawableSelected() {

        iv_healthSelected.setOnClickListener {
            iv_healthSelected.isSelected = !iv_healthSelected.isSelected
            drawableSelected = R.drawable.ic_baseline_favorite_24

            //makes sure that the user will only be able to pick one icon
            iv_mentalSelected.isSelected = false
            iv_productivitySelected.isSelected = false
        }

        iv_mentalSelected.setOnClickListener {
            iv_mentalSelected.isSelected = !iv_mentalSelected.isSelected
            drawableSelected = R.drawable.ic_baseline_self_improvement_24

            iv_healthSelected.isSelected = false
            iv_productivitySelected.isSelected = false
        }

        iv_productivitySelected.setOnClickListener {
            iv_productivitySelected.isSelected = !iv_productivitySelected.isSelected
            drawableSelected = R.drawable.ic_baseline_edit_24

            iv_healthSelected.isSelected = false
            iv_mentalSelected.isSelected = false
        }

    }

    //Get the time picked
    override fun onTimeSet(TimePicker: TimePicker?, hourX: Int, minuteX: Int) {
        Log.d("Fragment", "Time: $hourX:$minuteX")

        cleanTime = Calculations.formatTime(hourX, minuteX)
        tv_timeSelected.text = "Time: $cleanTime"
    }

    //Get the date picked
    override fun onDateSet(p0: DatePicker?, yearX: Int, monthX: Int, dayX: Int) {

        cleanDate = Calculations.formatDate(dayX, monthX, yearX)
        tv_dateSelected.text = "Date: $cleanDate"
    }

    //Return most recent hour and minute
    private fun getTimeCalendar() {
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    //Return most recent date
    private fun getDateCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

}