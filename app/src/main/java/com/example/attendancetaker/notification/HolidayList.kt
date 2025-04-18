package com.example.attendancetaker.notification

import java.time.LocalDate
import java.time.MonthDay

object HolidayList {

    val fixedHolidays = listOf(
        MonthDay.of(1, 1),   // New Year's Day - January 1
        MonthDay.of(1, 14),  // Makar Sankranti / Pongal - January 14
        MonthDay.of(1, 26),  // Republic Day - January 26
        MonthDay.of(8, 15),  // Independence Day - August 15
        MonthDay.of(9, 5),   // Teachers' Day - September 5
        MonthDay.of(10, 2),  // Gandhi Jayanti - October 2
        MonthDay.of(12, 25)  // Christmas - December 25
    )

    val holidays : List<String> = fixedHolidays.map { it.atYear(LocalDate.now().year).toString() }

}
