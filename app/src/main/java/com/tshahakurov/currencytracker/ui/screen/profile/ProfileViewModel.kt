package com.tshahakurov.currencytracker.ui.screen.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tshahakurov.currencytracker.app.logic.auth.SignInResult
import com.tshahakurov.currencytracker.app.logic.network.NetworkReceiver
import com.tshahakurov.currencytracker.data.model.UserData
import com.tshahakurov.currencytracker.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val screenState = MutableStateFlow<ScreenState>(ScreenState.NotLoggedIn)
    val isNetworkConnected = MutableStateFlow(false)

    fun startObservingNetworkState() {
        viewModelScope.launch {
            NetworkReceiver.isNetworkConnected.collect { isConnected ->
                isNetworkConnected.value = isConnected
            }
        }
    }

    fun checkLogin(user: UserData) {
        viewModelScope.launch(Dispatchers.IO) {
            if (userRepository.getUserByEmail(user.email)?.email != UserData.defaultUser.email) {
                screenState.value = ScreenState.LoggedIn
            } else
                screenState.value = ScreenState.NotLoggedIn
        }
    }

    fun loginOut() {
        screenState.value = ScreenState.NotLoggedIn
    }

    fun onSignInResult(result: SignInResult, onLoginUser: (UserData) -> Unit) {
        val user = result.data
        viewModelScope.launch(Dispatchers.IO) {
            if (user != null) {
                val dbUser = userRepository.getUserByEmail(user.email)
                if (dbUser == null)
                    userRepository.insertUser(user)
                onLoginUser(userRepository.getUserByEmail(user.email)!!)
                screenState.value = ScreenState.LoggedIn
            } else
                screenState.value = ScreenState.NotLoggedIn
        }
    }
}

sealed class ScreenState {
    data object LoggedIn : ScreenState()
    data object NotLoggedIn : ScreenState()
    data object Success : ScreenState()
    data object Empty : ScreenState()
    data object Loading : ScreenState()
}