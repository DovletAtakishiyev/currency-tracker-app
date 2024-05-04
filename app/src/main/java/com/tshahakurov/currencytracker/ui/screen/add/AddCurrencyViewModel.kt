package com.tshahakurov.currencytracker.ui.screen.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tshahakurov.currencytracker.app.logic.network.NetworkReceiver
import com.tshahakurov.currencytracker.data.model.CustomCurrency
import com.tshahakurov.currencytracker.data.model.converter.toCustomCurrencyArrayList
import com.tshahakurov.currencytracker.data.repository.CurrencyRepository
import com.tshahakurov.currencytracker.ui.screen.profile.ScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCurrencyViewModel @Inject constructor(
    private val currencyRepository: CurrencyRepository
) : ViewModel() {

    val currencyList = MutableStateFlow<ArrayList<CustomCurrency>>(arrayListOf())
    val isNetworkConnected = MutableStateFlow(false)
    val screenState = MutableStateFlow<ScreenState>(ScreenState.Empty)

    fun startObservingNetworkState() {
        viewModelScope.launch {
            NetworkReceiver.isNetworkConnected.collect { isConnected ->
                isNetworkConnected.value = isConnected
            }
        }
    }

    fun getSavedRates(){
        viewModelScope.launch(Dispatchers.IO) {
            screenState.value = ScreenState.Loading
            currencyList.value = currencyRepository.getLastSavedRates()
            if (currencyList.value.isEmpty())
                screenState.value = ScreenState.Empty
            else
                screenState.value = ScreenState.Success
        }
    }

    private fun saveRates(){
        viewModelScope.launch(Dispatchers.IO) {
            currencyRepository.saveRates(currencyList.value)
        }
    }

    fun getLatestRates() {
        viewModelScope.launch(Dispatchers.IO) {
            screenState.value = ScreenState.Loading
            val response = currencyRepository.getLatestRates()
            if (response.isSuccessful) {
                currencyList.value = response.body()?.toCustomCurrencyArrayList() ?: arrayListOf()
                saveRates()
            }
            screenState.value = ScreenState.Success
        }
    }
}