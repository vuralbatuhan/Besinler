package com.example.besinprojesi.util

import android.content.Context
import android.content.SharedPreferences

class SpecialSharedPreferences {

        companion object {

            private val TIME = "time"
            private var sharedPreferences : SharedPreferences? = null

            @Volatile
            private var instance: SpecialSharedPreferences? = null

            private val lock = Any()

            operator fun invoke(context: Context) = instance ?: synchronized(lock) {
                instance ?: specialSharedPreferencesOlustur(context).also {
                    instance = it
                }
            }

            private fun specialSharedPreferencesOlustur(context: Context) : SpecialSharedPreferences {
                sharedPreferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
                return SpecialSharedPreferences()
            }

        }

    fun saveTime(time : Long) {
        sharedPreferences?.edit()?.putLong(TIME,time)?.apply()
    }

    fun getTime() = sharedPreferences?.getLong(TIME,0)

}