package com.bignerdranch.android.nudge.habittracker.ui.fragments.updatehabit

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bignerdranch.android.nudge.R
import com.bignerdranch.android.nudge.habittracker.data.models.Habit
import com.bignerdranch.android.nudge.habittracker.logic.utils.Calculations
import com.bignerdranch.android.nudge.habittracker.ui.viewmodels.HabitViewModel

import kotlinx.android.synthetic.main.fragment_update_habit_item.*
import java.util.*

class UpdateHabitItem : Fragment(R.layout.fragment_update_habit_item),
        TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private var title = ""
    private var description = ""
    private var drawableSelected = 0
    private var timeStamp = ""

    private val args by navArgs<UpdateHabitItemArgs>()
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

        //Retrieve data from our habit list
        et_habitName_update.setText(args.selectedHabit.habit_title)
        et_habitDescription_update.setText(args.selectedHabit.habit_description)

        // Pick an icon
        drawableSelected()

        // Re-pick the date and time again
        pickDateAndTime()

        // Confirm changes and update the selected item
        btn_update.setOnClickListener {
            hideKeyboard()
            updateHabit()
        }

        // Show the options menu in this fragment
        setHasOptionsMenu(true)
    }

    private fun updateHabit() {
        //Get text from editTexts
        title = et_habitName_update.text.toString()
        description = et_habitDescription_update.text.toString()

        //Create a timestamp string for our recyclerview
        timeStamp = "$cleanDate $cleanTime"

        //Check that the form is complete before submitting data to the database
        if (!(title.isEmpty() || description.isEmpty() || timeStamp.isEmpty() || drawableSelected == 0)) {
            val habit =
                    Habit(args.selectedHabit.id, title, description, timeStamp, drawableSelected)

            //add the habit if all the fields are filled
            habitViewModel.updateHabit(habit)
            Toast.makeText(context, "Habit updated! successfully!", Toast.LENGTH_SHORT).show()

            //navigate back to our home fragment
            findNavController().navigate(R.id.action_updateHabitItem_to_habitList)
        } else {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        }
    }

    // Create a selector for our icons which will appear in the recycler view
    private fun drawableSelected() {

        iv_healthSelected_update.setOnClickListener {
            iv_healthSelected_update.isSelected = !iv_healthSelected_update.isSelected
            drawableSelected = R.drawable.ic_baseline_favorite_24

            //makes sure that the user will only be able to pick one icon
            iv_mentalSelected_update.isSelected = false
            iv_productivitySelected_update.isSelected = false
        }

        iv_mentalSelected_update.setOnClickListener {
            iv_mentalSelected_update.isSelected = !iv_mentalSelected_update.isSelected
            drawableSelected = R.drawable.ic_baseline_self_improvement_24

            iv_healthSelected_update.isSelected = false
            iv_productivitySelected_update.isSelected = false
        }

        iv_productivitySelected_update.setOnClickListener {
            iv_productivitySelected_update.isSelected = !iv_productivitySelected_update.isSelected
            drawableSelected = R.drawable.ic_baseline_edit_24

            iv_healthSelected_update.isSelected = false
            iv_mentalSelected_update.isSelected = false
        }
    }
    //------------------------------------------

    //Handle date and time picking
    @RequiresApi(Build.VERSION_CODES.N)
    //set on click listeners for our data and time pickers
    private fun pickDateAndTime() {
        btn_pickDate_update.setOnClickListener {
            hideKeyboard()
            getDateCalendar()
            DatePickerDialog(requireContext(), this, year, month, day).show()
        }

        btn_pickTime_update.setOnClickListener {
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

    private fun getTimeCalendar() {
        val cal = Calendar.getInstance()
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun getDateCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    override fun onTimeSet(p0: TimePicker?, p1: Int, p2: Int) {
        cleanTime = Calculations.formatTime(p1, p2)
        tv_timeSelected_update.text = "Time: $cleanTime"
    }

    override fun onDateSet(p0: DatePicker?, yearX: Int, monthX: Int, dayX: Int) {
        cleanDate = Calculations.formatDate(dayX, monthX, yearX)
        tv_dateSelected_update.text = "Date: $cleanDate"
    }

    // For deleting...

    //Create options menu
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.single_item_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_delete -> {
                deleteHabit(args.selectedHabit)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //Delete a single Habit
    private fun deleteHabit(habit: Habit) {
        habitViewModel.deleteHabit(habit)
        Toast.makeText(context, "Habit successfully deleted!", Toast.LENGTH_SHORT).show()

        findNavController().navigate(R.id.action_updateHabitItem_to_habitList)
    }
}