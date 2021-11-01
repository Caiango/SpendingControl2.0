package com.example.spendingcontrol20.utils

import android.content.Context

object SharedPref {
    fun saveCurrentUser(context: Context, currentUser: String) {
        val sharedPref = context.getSharedPreferences(
            "mySP", Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putString(Constants.CURRENT_USER, currentUser)
            commit()
        }
    }

    fun saveCurrentMonth(context: Context, currentMonth: Int) {
        val sharedPref = context.getSharedPreferences(
            "mySP", Context.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            putInt(Constants.CURRENT_MONTH, currentMonth)
            commit()
        }
    }

    fun getCurrentUserId(context: Context): String {
        val sharedPref = context.getSharedPreferences(
            "mySP", Context.MODE_PRIVATE
        )
        return sharedPref.getString(Constants.CURRENT_USER, "")!!
    }

    fun getCurrentMonth(context: Context): Int {
        val sharedPref = context.getSharedPreferences(
            "mySP", Context.MODE_PRIVATE
        )
        return sharedPref.getInt(Constants.CURRENT_MONTH, 0)
    }
}