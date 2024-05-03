package com.tshahakurov.currencytracker.ui.screen.add

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.tshahakurov.currencytracker.data.model.CustomCurrency
import com.tshahakurov.currencytracker.data.model.UserData
import com.tshahakurov.currencytracker.ui.navigation.AppBar
import com.tshahakurov.currencytracker.ui.navigation.CurrencyScreens
import com.tshahakurov.currencytracker.ui.theme.DP_1
import com.tshahakurov.currencytracker.ui.theme.DP_10
import com.tshahakurov.currencytracker.ui.theme.DP_16
import com.tshahakurov.currencytracker.ui.theme.DP_30
import com.tshahakurov.currencytracker.ui.theme.DP_4
import com.tshahakurov.currencytracker.ui.theme.DP_50
import com.tshahakurov.currencytracker.ui.theme.DP_64
import com.tshahakurov.currencytracker.ui.theme.DP_70

@Composable
fun AddCurrencyScreen(
    user: UserData = UserData.defaultUser,
    viewModel: AddCurrencyViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(),
    onBackPressed: () -> Unit = {},
    onCurrencyAdded: (CustomCurrency) -> Unit = {},
    onCurrencyRemoved: (CustomCurrency) -> Unit = {},
) {
    val currencyList by viewModel.currencyList.collectAsState()

    viewModel.getSavedRates()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            AppBar(
                screen = CurrencyScreens.AddCurrency,
                onBackPressed = onBackPressed,
                onUpdatedClicked = { viewModel.getLatestRates() }
            )
            AddCurrencyScreenElements(user, currencyList, onCurrencyAdded, onCurrencyRemoved)
        }
    }
}

@Composable
fun AddCurrencyScreenElements(
    user: UserData,
    list: ArrayList<CustomCurrency>,
    onCurrencyAdded: (CustomCurrency) -> Unit,
    onCurrencyRemoved: (CustomCurrency) -> Unit,
) {
    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
        list.forEach { currency ->
            AvailableCurrencyElement(
                currency,
                user.activeCurrencies.any { it.code == currency.code },
                onCurrencyAdded,
                onCurrencyRemoved
            )
        }
        Spacer(modifier = Modifier.requiredHeight(DP_64))
    }
}

@Composable
fun AvailableCurrencyElement(
    currency: CustomCurrency,
    isActive: Boolean,
    onCurrencyAdded: (CustomCurrency) -> Unit,
    onCurrencyRemoved: (CustomCurrency) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(DP_70)
            .padding(DP_4)
            .clip(RoundedCornerShape(DP_10))
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DP_16),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .requiredHeight(DP_30)
                        .requiredWidth(DP_50)
                        .border(
                            border = BorderStroke(DP_1, Color.Gray),
                            shape = RoundedCornerShape(DP_10)
                        )
                        .padding(DP_4),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = currency.getSymbol())
                }
                Column(
                    modifier = Modifier.padding(start = DP_10)
                ) {
                    Text(text = currency.code)
                    Text(text = currency.toString())
                }
            }

            var isChecked by remember { mutableStateOf(isActive) }
            Checkbox(checked = isChecked, onCheckedChange = {
                isChecked = !isChecked
                if (isChecked)
                    onCurrencyAdded(currency)
                else
                    onCurrencyRemoved(currency)
            })
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAddCurrencyScreen() {
    AddCurrencyScreen()
}
