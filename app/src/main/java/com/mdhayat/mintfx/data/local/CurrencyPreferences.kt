package com.mdhayat.mintfx.data.local

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class CurrencyPreferences(context: Context) {
    private val preferences = context.applicationContext.getSharedPreferences(
        PREFERENCES_NAME,
        Context.MODE_PRIVATE
    )
    private val gson = Gson()

    fun saveRates(rates: Map<String, Double>) {
        preferences.edit()
            .putString(RATES_KEY, gson.toJson(rates))
            .apply()
    }

    fun readRates(): Map<String, Double> {
        val json = preferences.getString(RATES_KEY, null) ?: return emptyMap()
        return runCatching {
            val type = object : TypeToken<Map<String, Double>>() {}.type
            gson.fromJson<Map<String, Double>>(json, type) ?: emptyMap()
        }.getOrDefault(emptyMap())
    }

    private companion object {
        const val PREFERENCES_NAME = "mintfx_preferences"
        const val RATES_KEY = "cached_rates"
    }
}