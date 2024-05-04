package com.tshahakurov.currencytracker.ui.screen.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tshahakurov.currencytracker.data.model.CustomCurrency
import com.tshahakurov.currencytracker.data.model.UserData
import com.tshahakurov.currencytracker.data.repository.UserRepository
import com.tshahakurov.currencytracker.ui.screen.profile.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val screenState = MutableStateFlow<ScreenState>(ScreenState.NotLoggedIn)

    fun checkLogin(user: UserData) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userRepository.getUserByEmail(user.email)?.email != UserData.defaultUser.email)
                screenState.value = ScreenState.LoggedIn
            else
                screenState.value = ScreenState.NotLoggedIn
        }
    }

    fun removeCurrency(user: UserData, currency: CustomCurrency) {
        viewModelScope.launch(Dispatchers.IO) {
            val currencyToRemove = user.activeCurrencies.find { it.code == currency.code }
            if (currencyToRemove != null) {
                user.activeCurrencies.remove(currencyToRemove)
            }
        }
    }
}