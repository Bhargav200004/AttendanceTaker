package com.example.attendancetaker.utils.functions

object ValidationFunction {
    fun isValidPassword(password: String): Boolean {
        val regex = "^[A-Za-z0-9!@#\$%^&*()_+\\-=]{3,8}$".toRegex()
        return regex.matches(password)
    }

    fun isValidEmail(email: String): Boolean {
        val regex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return regex.matches(email)
    }
}