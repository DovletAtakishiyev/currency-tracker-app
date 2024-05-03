package com.tshahakurov.currencytracker.ui.navigation

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tshahakurov.currencytracker.R
import com.tshahakurov.currencytracker.data.model.UserData
import com.tshahakurov.currencytracker.ui.screen.add.AddCurrencyScreen
import com.tshahakurov.currencytracker.ui.screen.main.MainScreen
import com.tshahakurov.currencytracker.ui.screen.profile.ProfileScreen
import com.tshahakurov.currencytracker.ui.screen.settings.SettingsScreen

enum class CurrencyScreens(@StringRes val route: Int) {
    Main(R.string.main),
    Settings(R.string.settings),
    AddCurrency(R.string.add_currency),
    Profile(R.string.profile),
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyApp(
    viewModel: NavigationViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    viewModel.getUser()

    Scaffold {
        NavHost(
            navController = navController,
            startDestination = CurrencyScreens.Main.name
        ) {
            // Main Screen
            composable(
                route = CurrencyScreens.Main.name
            ) {
                MainScreen(
                    currentUser,
                    onAddCurrencyClicked = {
                        navController.navigate(CurrencyScreens.AddCurrency.name)
                    },
                    onProfileClicked = { navController.navigate(CurrencyScreens.Profile.name) },
                    onSettingsClicked = { navController.navigate(CurrencyScreens.Settings.name) },
                )
            }

            // Settings Screen
            composable(
                route = CurrencyScreens.Settings.name
            ) {
                SettingsScreen(
                    onBackPressed = { navController.popBackStack() }
                )
            }

            // Add Currency Screen
            composable(
                route = CurrencyScreens.AddCurrency.name
            ) {
                AddCurrencyScreen(
                    currentUser,
                    onBackPressed = { navController.popBackStack() },
                    onCurrencyAdded = { viewModel.addCurrency(it) },
                    onCurrencyRemoved = { viewModel.removeCurrency(it) }
                )
            }
            // Profile Screen
            composable(
                route = CurrencyScreens.Profile.name
            ) {
                ProfileScreen(
                    currentUser,
                    onLoginUser = { newUser: UserData -> viewModel.setCurrentUser(newUser) },
                    onRemoveUser = { viewModel.removeUser() },
                    onBackPressed = { navController.popBackStack() }
                )
            }
        }
    }
}
