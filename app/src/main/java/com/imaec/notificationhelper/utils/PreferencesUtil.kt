package com.imaec.notificationhelper.utils

import android.content.Context
import com.imaec.notificationhelper.PREF_NAME
import com.imaec.notificationhelper.PrefKey

object PreferencesUtil {

    fun put(context: Context, key: PrefKey, value: String) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().apply {
            putString(key.name, value)
            apply()
        }
    }

    fun put(context: Context, key: PrefKey, value: Int) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().apply {
            putInt(key.name, value)
            apply()
        }
    }

    fun put(context: Context, key: PrefKey, value: Boolean) {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        pref.edit().apply {
            putBoolean(key.name, value)
            apply()
        }
    }

    fun getString(context: Context, key: PrefKey, def: String = "") : String {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getString(key.name, def) ?: ""
    }

    fun getInt(context: Context, key: PrefKey, def: Int = 0) : Int {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getInt(key.name, def)
    }

    fun getBool(context: Context, key: PrefKey, def: Boolean = false) : Boolean {
        val pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return pref.getBoolean(key.name, def)
    }
}