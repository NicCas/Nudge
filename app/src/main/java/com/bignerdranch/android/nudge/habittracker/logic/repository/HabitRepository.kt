package com.bignerdranch.android.nudge.logic.repository

import androidx.lifecycle.LiveData
import com.bignerdranch.android.nudge.habittracker.data.models.Habit
import com.bignerdranch.android.nudge.habittracker.logic.dao.HabitDao


class HabitRepository (private val habitDao: HabitDao) {
    val getAllHabits: LiveData<List<Habit>> = habitDao.getAllHabits()

    suspend fun addHabit(habit: Habit) {
        habitDao.addHabit(habit)
    }

    suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(habit)
    }

    suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(habit)
    }

    suspend fun deleteAllHabits() {
        habitDao.deleteAll()
    }
}