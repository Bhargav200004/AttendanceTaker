package com.example.attendancetaker.notification

import java.time.DayOfWeek
import java.time.LocalDate

// Check if the day is working day or not
fun isWorkingDay(date: LocalDate, holidays: List<String>) : Boolean {
    // Checking if the day is sunday or holiday
    val isSunday = date.dayOfWeek === DayOfWeek.SUNDAY

    // Checking if the day is holiday or not
    val isHoliday = holidays.contains(date.toString())

    // Returning the result
    return !isSunday && !isHoliday
}