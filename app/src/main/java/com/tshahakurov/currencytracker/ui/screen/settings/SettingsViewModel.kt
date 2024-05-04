package com.tshahakurov.currencytracker.ui.screen.settings

import androidx.lifecycle.ViewModel
import com.tshahakurov.currencytracker.data.repository.SharedPreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferencesRepository
) : ViewModel() {

    val languageList = listOf("English" to "eng", "Russian" to "ru", "German" to "de")

    fun saveChanges(locale: String) {
        sharedPreferences.setLanguage(locale)
    }

    fun getLocaleFull(): String {
        val locale = sharedPreferences.getLanguage()
        val matchingLanguage = languageList.find { it.second == locale }
        return matchingLanguage?.first ?: "English"
    }
}