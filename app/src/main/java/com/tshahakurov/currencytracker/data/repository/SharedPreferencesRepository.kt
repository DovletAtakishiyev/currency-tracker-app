package com.tshahakurov.currencytracker.data.repository

import android.content.Context
import android.provider.Settings.Global.putString
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

const val APP_SHARED_PREF = "app_shared_pref"
const val APP_LANGUAGE = "locale"
const val DEFAULT_LANGUAGE = "eng"

class SharedPreferencesRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    private val appPreferences = context.getSharedPreferences(APP_SHARED_PREF, Context.MODE_PRIVATE)

    fun getLanguage() = appPreferences.getString(APP_LANGUAGE, DEFAULT_LANGUAGE)

    fun setLanguage(locale: String) {
        appPreferences.edit { putString(APP_LANGUAGE, locale) }
    }
}