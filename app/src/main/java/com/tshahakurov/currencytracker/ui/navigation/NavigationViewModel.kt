package com.tshahakurov.currencytracker.ui.navigation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.tshahakurov.currencytracker.data.model.CustomCurrency
import com.tshahakurov.currencytracker.data.model.UserData
import com.tshahakurov.currencytracker.data.repository.SharedPreferencesRepository
import com.tshahakurov.currencytracker.data.repository.UserRepository
import com.tshahakurov.currencytracker.util.toUserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val sharedPreferences: SharedPreferencesRepository
) : ViewModel() {

    val currentUser = MutableStateFlow(UserData("none", "none", null, arrayListOf()))

    fun getUser() {
        viewModelScope.launch(Dispatchers.IO) {
            if (Firebase.auth.currentUser != null) {
                currentUser.value =
                    userRepository.getUserByEmail(Firebase.auth.toUserData().email)!!
            } else {
                if (userRepository.getUserByEmail(UserData.defaultUser.email) == null) {
                    userRepository.insertUser(UserData.defaultUser)
                }
                currentUser.value = userRepository.getUserByEmail(UserData.defaultUser.email)
                    ?: UserData.defaultUser
            }
        }
    }

    fun setCurrentUser(newUserData: UserData) {
        viewModelScope.launch(Dispatchers.IO) {
            val loggedInUser = userRepository.getUserByEmail(newUserData.email)
            if (loggedInUser == null)
                userRepository.insertUser(newUserData)
            currentUser.value = newUserData
        }
    }

    fun removeUser() {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser.value = userRepository.getUserByEmail(UserData.defaultUser.email)!!
        }
    }

    fun addCurrency(currency: CustomCurrency) {
        viewModelScope.launch(Dispatchers.IO) {
            currentUser.value.activeCurrencies.add(currency)
            currentUser.value.activeCurrencies.sortBy { it.code }
            userRepository.updateUser(currentUser.value)
        }
    }

    fun removeCurrency(currency: CustomCurrency) {
        viewModelScope.launch(Dispatchers.IO) {
            val currencyToRemove =
                currentUser.value.activeCurrencies.find { it.code == currency.code }
            if (currencyToRemove != null) {
                currentUser.value.activeCurrencies.remove(currencyToRemove)
            }
            userRepository.updateUser(currentUser.value)
        }
    }

    fun getLocale() = sharedPreferences.getLanguage()
}

