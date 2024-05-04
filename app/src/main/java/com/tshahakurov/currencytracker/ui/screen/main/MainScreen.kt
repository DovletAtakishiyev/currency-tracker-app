package com.tshahakurov.currencytracker.ui.screen.main

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tshahakurov.currencytracker.R
import com.tshahakurov.currencytracker.data.model.CustomCurrency
import com.tshahakurov.currencytracker.data.model.UserData
import com.tshahakurov.currencytracker.ui.navigation.AppBar
import com.tshahakurov.currencytracker.ui.navigation.CurrencyScreens
import com.tshahakurov.currencytracker.ui.screen.NumberKeyboard
import com.tshahakurov.currencytracker.ui.theme.DP_1
import com.tshahakurov.currencytracker.ui.theme.DP_10
import com.tshahakurov.currencytracker.ui.theme.DP_14
import com.tshahakurov.currencytracker.ui.theme.DP_2
import com.tshahakurov.currencytracker.ui.theme.DP_30
import com.tshahakurov.currencytracker.ui.theme.DP_4
import com.tshahakurov.currencytracker.ui.theme.DP_40
import com.tshahakurov.currencytracker.ui.theme.DP_50
import com.tshahakurov.currencytracker.ui.theme.DP_500
import com.tshahakurov.currencytracker.ui.theme.DP_6
import com.tshahakurov.currencytracker.ui.theme.DP_70
import com.tshahakurov.currencytracker.util.dashedBorder
import kotlinx.coroutines.launch
import java.lang.Math.round

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen(
    user: UserData = UserData.defaultUser,
    viewModel: MainViewModel = hiltViewModel(),
    onSettingsClicked: () -> Unit = {},
    onAddCurrencyClicked: () -> Unit = {},
    onProfileClicked: () -> Unit = {},
) {
    val state by viewModel.screenState.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    viewModel.checkLogin(user)

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                MenuElement(
                    state = state,
                    onSettingsClicked = onSettingsClicked,
                    onProfileClicked = onProfileClicked
                )
            }
        },
        drawerState = drawerState
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            Column {
                AppBar(
                    screen = CurrencyScreens.Main,
                    onMenuItemClicked = { scope.launch { drawerState.open() } }
                )
                ActiveCurrencyList(user.activeCurrencies, onAddCurrencyClicked)
            }
        }

    }
}

@Composable
fun ActiveCurrencyList(list: ArrayList<CustomCurrency>, onAddCurrencyClicked: () -> Unit) {
    var selectedIndex by remember { mutableStateOf(-1) }
    var isNumPadVisible by remember { mutableStateOf(false) }
    var amountFloat by remember { mutableStateOf(1f) }
    var amountString by remember { mutableStateOf("1") }
    var baseCurrency by remember { mutableStateOf(CustomCurrency.defaultCurrency) }

    Box(Modifier.fillMaxSize()) {
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {

            list.forEachIndexed { id, currency ->
                val isSelected = selectedIndex == id
                ActiveCurrencyElement(amountFloat, baseCurrency, isSelected, currency) {
                    selectedIndex = if (selectedIndex == id) -1 else id
                    if (selectedIndex != -1){
                        isNumPadVisible = true
                        baseCurrency = list[selectedIndex]
                    } else
                        isNumPadVisible = false
                }
            }
            AddNewCurrencyElement(onClick = onAddCurrencyClicked)
            if (isNumPadVisible)
                Spacer(modifier = Modifier.requiredHeight(DP_500))
        }
        if (isNumPadVisible)
            NumberKeyboard(
                onKeyPressed = { keyPressed ->
                    if ("." == keyPressed) {
                        if (amountFloat - amountFloat.toInt() == 0f) {
                            amountString += keyPressed
                        }
                    } else {
                        amountString += keyPressed
                    }
                    amountFloat = amountString.toFloatOrNull() ?: 0f
                },
                onDeletePressed = {
                    if (amountString.isNotEmpty()) {
                        amountString = amountString.dropLast(1)
                        amountFloat = amountString.toFloatOrNull() ?: 0f
                    }
                },
                onExitPressed = { isNumPadVisible = false }
            )
    }
}

@Composable
fun ActiveCurrencyElement(
    amount: Float,
    baseCurrency: CustomCurrency,
    isSelected: Boolean,
    currency: CustomCurrency,
    onClick: () -> Unit = {},
) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(DP_70)
            .padding(DP_4)
            .clip(RoundedCornerShape(DP_10))
            .background(if (isSelected) Color.LightGray else Color.White)
            .border(
                border = BorderStroke(DP_1, if (isSelected) Color.LightGray else Color.Black),
                shape = RoundedCornerShape(DP_10)
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DP_14),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .requiredHeight(DP_40)
                        .requiredWidth(DP_50)
                        .border(BorderStroke(DP_1, Color.Black), RoundedCornerShape(DP_10))
                        .padding(DP_2),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = currency.getSymbol())
                }

                Column(
                    modifier = Modifier.padding(DP_6)
                ) {
                    Text(text = currency.code)
                    Text(text = currency.toString())
                }
            }
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = currency.getSymbol())
                Text(text = "${
                    round(currency.calculateAmount(baseCurrency, amount) * 100f) / 100f
                }")
            }

        }
    }
}

@Composable
fun AddNewCurrencyElement(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(DP_70)
            .padding(DP_4)
            .clip(RoundedCornerShape(DP_10))
            .dashedBorder(
                width = DP_1,
                color = Color.Gray,
                shape = RoundedCornerShape(DP_10),
                on = DP_4, off = DP_4
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(R.drawable.ic_add),
                modifier = Modifier
                    .requiredHeight(DP_30)
                    .requiredWidth(DP_30)
                    .padding(DP_2),
                contentDescription = ""
            )
            Text(text = stringResource(R.string.add_new), modifier = Modifier.padding(DP_2))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenElement() {
    MainScreen()
}