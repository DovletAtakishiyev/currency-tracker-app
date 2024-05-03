package com.tshahakurov.currencytracker.ui.screen.add

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tshahakurov.currencytracker.app.TAG
import com.tshahakurov.currencytracker.data.model.CustomCurrency
import com.tshahakurov.currencytracker.data.model.converter.toCustomCurrencyArrayList
import com.tshahakurov.currencytracker.data.repository.CurrencyRepository
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

    fun getSavedRates(){
        viewModelScope.launch(Dispatchers.IO) {
            currencyList.value = currencyRepository.getLastSavedRates()
        }
    }

    private fun saveRates(){
        viewModelScope.launch(Dispatchers.IO) {
            currencyRepository.saveRates(currencyList.value)
        }
    }

    fun getLatestRates() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = currencyRepository.getLatestRates()
            if (response.isSuccessful) {
                currencyList.value = response.body()?.toCustomCurrencyArrayList() ?: arrayListOf()
                saveRates()
            } else {
                Log.d(TAG, "error - " + response.message())
            }
        }
    }
}