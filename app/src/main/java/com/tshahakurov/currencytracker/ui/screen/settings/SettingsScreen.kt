package com.tshahakurov.currencytracker.ui.screen.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.tshahakurov.currencytracker.R
import com.tshahakurov.currencytracker.ui.navigation.AppBar
import com.tshahakurov.currencytracker.ui.navigation.CurrencyScreens
import com.tshahakurov.currencytracker.ui.theme.DP_1
import com.tshahakurov.currencytracker.ui.theme.DP_10
import com.tshahakurov.currencytracker.ui.theme.DP_100
import com.tshahakurov.currencytracker.ui.theme.DP_50
import com.tshahakurov.currencytracker.ui.theme.DP_60
import com.tshahakurov.currencytracker.ui.theme.DP_8
import java.util.Locale

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    paddingValues: PaddingValues = PaddingValues(),
    onBackPressed: () -> Unit = {}
) {

    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.TopCenter
    ) {
        Column {
            AppBar(screen = CurrencyScreens.Settings, onBackPressed = onBackPressed)
            // --- --- --- --- Theme --- --- --- --- //
            SettingsElement(
                icon = painterResource(R.drawable.ic_theme),
                title = stringResource(R.string.theme),
                value = "value"
            )

            // --- --- --- --- Language --- --- --- --- //
            var showLanguageDialog by remember { mutableStateOf(false) }
            var languageValue by remember { mutableStateOf("English" to "eng") }
            SettingsElement(
                icon = painterResource(R.drawable.ic_language),
                title = stringResource(R.string.language),
                value = languageValue.first
            ) {
                showLanguageDialog = true
            }
            if (showLanguageDialog)
                LanguageDialog(
                    title = stringResource(R.string.language),
                    onDismiss = {
                        viewModel.saveChanges()
                        showLanguageDialog = false
                    },
                    onLanguageChosen = { newLanguage ->
                        viewModel.saveChanges()
                        val locale = Locale(newLanguage.second)
                        Locale.setDefault(locale)
                        val resources = context.resources
                        val configuration = resources.configuration
                        configuration.setLocale(locale)
                        resources.updateConfiguration(configuration, resources.displayMetrics)
                        languageValue = newLanguage
                    }
                )

            // --- --- --- --- Display Size --- --- --- --- //
            SettingsElement(
                icon = painterResource(R.drawable.ic_tsize),
                title = stringResource(R.string.display_size),
                value = "value"
            )

            // --- --- --- --- Decimal Precision --- --- --- --- //
            SettingsElement(
                icon = painterResource(R.drawable.ic_precision),
                title = stringResource(R.string.precision),
                value = "value"
            )
        }
    }
}

@Composable
fun LanguageDialog(
    title: String,
    onDismiss: () -> Unit = {},
    onLanguageChosen: (Pair<String, String>) -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight(0.6f)
                .clip(RoundedCornerShape(DP_10))
        ) {
            Column(
                modifier = Modifier.padding(DP_8),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = title, modifier = Modifier.padding(DP_8))
                    val langList = listOf("English" to "eng", "Russian" to "ru")
                    langList.forEach {
                        LanguageElement(it, onLanguageChosen)
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.CenterEnd
                )
                {
                    TextButton(
                        onClick = onDismiss,
                    ) {
                        Text(text = stringResource(R.string.close))
                    }
                }
            }
        }
    }
}

@Composable
fun LanguageElement(
    language: Pair<String, String>,
    onClick: (Pair<String, String>) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(DP_50)
            .padding(DP_8)
            .clickable { onClick(language) },
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(modifier = Modifier, text = language.first)
        }

    }
}

@Composable
fun SettingsElement(
    icon: Painter,
    title: String,
    value: String,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(DP_100)
            .padding(horizontal = DP_10),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(modifier = Modifier.clickable { onClick() }) {
            Row(
                modifier = Modifier
                    .padding(bottom = DP_10, end = DP_10)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = icon,
                    contentDescription = "",
                    modifier = Modifier.size(DP_60).padding(end = DP_10)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = title)
                    Text(text = value)
                }
            }
            Divider(color = Color.LightGray, thickness = DP_1)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingsScreen() {
    SettingsScreen()
}