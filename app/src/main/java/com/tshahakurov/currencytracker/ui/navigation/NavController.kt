package com.tshahakurov.currencytracker.ui.navigation

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navDeepLink
import com.tshahakurov.currencytracker.R
import com.tshahakurov.currencytracker.data.model.UserData
import com.tshahakurov.currencytracker.ui.screen.add.AddCurrencyScreen
import com.tshahakurov.currencytracker.ui.screen.main.MainScreen
import com.tshahakurov.currencytracker.ui.screen.profile.ProfileScreen
import com.tshahakurov.currencytracker.ui.screen.settings.SettingsScreen
import java.util.Locale

enum class CurrencyScreens(@StringRes val route: Int) {
    Main(R.string.main),
    Settings(R.string.settings),
    AddCurrency(R.string.add_currency),
    Profile(R.string.profile),
}

private const val URI_PATTERN = "https://www.suita123.com"

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrencyApp(
    viewModel: NavigationViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController()
) {
    val currentUser by viewModel.currentUser.collectAsState()
    viewModel.getUser()

    viewModel.getLocale()?.let {
        Locale.setDefault(Locale(it))
        val resources = LocalContext.current.resources
        val configuration = resources.configuration
        configuration.setLocale(Locale(it))
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }

    Scaffold {
        NavHost(
            navController = navController,
            startDestination = CurrencyScreens.Main.name
        ) {
            // Main Screen
            composable(
                route = CurrencyScreens.Main.name,
                deepLinks = listOf(navDeepLink { uriPattern = "${URI_PATTERN}/{id}" })
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
                    onBackPressed = {
                        navController.popBackStack()
                    }
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
