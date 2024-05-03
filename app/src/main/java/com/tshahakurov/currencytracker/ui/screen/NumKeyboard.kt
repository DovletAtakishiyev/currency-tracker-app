package com.tshahakurov.currencytracker.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.tshahakurov.currencytracker.ui.theme.DP_1
import com.tshahakurov.currencytracker.ui.theme.DP_10
import com.tshahakurov.currencytracker.ui.theme.DP_100
import com.tshahakurov.currencytracker.ui.theme.DP_4
import com.tshahakurov.currencytracker.ui.theme.DP_40

@Composable
fun NumberKeyboard(
    onKeyPressed: (String) -> Unit = {},
    onDeletePressed: () -> Unit = {},
    onExitPressed: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = DP_10, topEnd = DP_10))
                .background(Color(0xFFEEEEEE))
                .padding(bottom = DP_10)
                .focusable(true)
                .clickable {  }
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .requiredHeight(DP_40)
                        .border(
                            border = BorderStroke(DP_1, Color.Black),
                            shape = RoundedCornerShape(DP_10)
                        )
                        .clickable { onExitPressed() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Exit input mode.")
                }
                Row {
                    NumberButton(text = "1", onClick = { onKeyPressed("1") })
                    NumberButton(text = "2", onClick = { onKeyPressed("2") })
                    NumberButton(text = "3", onClick = { onKeyPressed("3") })
                }
                Row {
                    NumberButton(text = "4", onClick = { onKeyPressed("4") })
                    NumberButton(text = "5", onClick = { onKeyPressed("5") })
                    NumberButton(text = "6", onClick = { onKeyPressed("6") })
                }
                Row {
                    NumberButton(text = "7", onClick = { onKeyPressed("7") })
                    NumberButton(text = "8", onClick = { onKeyPressed("8") })
                    NumberButton(text = "9", onClick = { onKeyPressed("9") })
                }
                Row {
                    NumberButton(text = ".", onClick = { onKeyPressed(".") })
                    NumberButton(text = "0", onClick = { onKeyPressed("0") })
                    NumberButton(text = "del", onClick = { onDeletePressed() })
                }
            }
        }

    }
}

@Composable
fun NumberButton(
    text: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .requiredHeight(DP_100)
            .requiredWidth(DP_100)
            .padding(DP_4)
            .border(border = BorderStroke(DP_1, Color.Black), shape = RoundedCornerShape(DP_10))
            .clip(RoundedCornerShape(DP_10))
            .background(Color.White)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = text)
    }
}

@Preview
@Composable
fun PreviewNumKeyboard() {
    NumberKeyboard()
}