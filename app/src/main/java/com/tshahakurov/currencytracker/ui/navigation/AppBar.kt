package com.tshahakurov.currencytracker.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.tshahakurov.currencytracker.R
import com.tshahakurov.currencytracker.ui.theme.DP_60
import com.tshahakurov.currencytracker.ui.theme.DP_8

@Composable
fun AppBar(
    screen: CurrencyScreens,
    onMenuItemClicked: () -> Unit = {},
    onBackPressed: () -> Unit = {},
    onUpdatedClicked: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(DP_60)
            .background(Color.LightGray),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = DP_8)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = when (screen) {
                        CurrencyScreens.Main -> onMenuItemClicked
                        else -> onBackPressed
                    }
                ) {
                    when (screen) {
                        CurrencyScreens.Main -> Icon(
                            imageVector = Icons.Filled.Menu,
                            contentDescription = ""
                        )

                        else -> Icon(
                            painter = painterResource(R.drawable.ic_back),
                            contentDescription = ""
                        )
                    }
                }
                Text(text = stringResource(id = screen.route))
            }
            if (screen == CurrencyScreens.AddCurrency)
                IconButton(onClick = onUpdatedClicked) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_update),
                        contentDescription = ""
                    )
                }
        }
    }
}
