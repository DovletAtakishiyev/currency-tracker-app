package com.tshahakurov.currencytracker.ui.screen.main

import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.tshahakurov.currencytracker.R
import com.tshahakurov.currencytracker.data.model.UserData
import com.tshahakurov.currencytracker.ui.screen.profile.ProfileState
import com.tshahakurov.currencytracker.ui.theme.DP_1
import com.tshahakurov.currencytracker.ui.theme.DP_100
import com.tshahakurov.currencytracker.ui.theme.DP_20
import com.tshahakurov.currencytracker.ui.theme.DP_4
import com.tshahakurov.currencytracker.ui.theme.DP_40
import com.tshahakurov.currencytracker.ui.theme.DP_50
import com.tshahakurov.currencytracker.ui.theme.DP_8
import com.tshahakurov.currencytracker.util.toUserData

@Composable
fun MenuElement(
    state: ProfileState,
    onSettingsClicked: () -> Unit = {},
    onProfileClicked: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0x55555555)),
        contentAlignment = Alignment.CenterEnd
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(top = DP_40),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                // --- --- --- --- Profile --- --- --- --- //
                ProfileElement(
                    user = Firebase.auth.toUserData(),
                    state = state,
                    onClick = onProfileClicked
                )

                Column(Modifier.padding(top = DP_20)) {
                    // --- --- --- --- Settings --- --- --- --- //
                    MenuItem(
                        icon = painterResource(R.drawable.ic_settings),
                        title = stringResource(R.string.settings),
                        onClick = onSettingsClicked
                    )
                    // --- --- --- --- GooglePlay --- --- --- --- //
                    MenuItem(
                        icon = painterResource(R.drawable.ic_rate),
                        title = stringResource(R.string.rate_google_play)
                    )
                    // --- --- --- --- Share --- --- --- --- //
                    val shareActivityResultLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.StartActivityForResult()
                    ) { }
                    val message = stringResource(id = R.string.share_message)
                    MenuItem(
                        icon = painterResource(R.drawable.ic_share),
                        title = stringResource(R.string.share)
                    ){
                        val intent = Intent(Intent.ACTION_SEND)
                        intent.type = "text/plain"
                        intent.putExtra(Intent.EXTRA_TEXT, message)

                        val chooserIntent = Intent.createChooser(intent, "Share message")

                        shareActivityResultLauncher.launch(chooserIntent)
                    }
                    // --- --- --- --- Feedback --- --- --- --- //
                    var showFeedbackDialog by remember { mutableStateOf(false) }
                    MenuItem(
                        icon = painterResource(R.drawable.ic_support),
                        title = stringResource(R.string.feed_and_supp)
                    ) {
                        showFeedbackDialog = true
                    }
                    if (showFeedbackDialog)
                        CustomAlertDialog(
                            title = stringResource(R.string.feed_and_supp),
                            body = stringResource(id = R.string.feedback_body),
                            onDismiss = { showFeedbackDialog = false }
                        )

                    // --- --- --- --- Policy --- --- --- --- //
                    var showPolicyDialog by remember { mutableStateOf(false) }
                    MenuItem(
                        icon = painterResource(R.drawable.ic_policy),
                        title = stringResource(R.string.policy)
                    ) {
                        showPolicyDialog = true
                    }
                    if (showPolicyDialog)
                        CustomAlertDialog(
                            title = stringResource(R.string.policy),
                            body = stringResource(id = R.string.privacy_policy_body),
                            onDismiss = { showPolicyDialog = false }
                        )
                    // --- --- --- --- About --- --- --- --- //
                    var showAboutDialog by remember { mutableStateOf(false) }
                    MenuItem(
                        icon =  painterResource(R.drawable.ic_about),
                        title = stringResource(R.string.about)
                    ) {
                        showAboutDialog = true
                    }
                    if (showAboutDialog)
                        CustomAlertDialog(
                            title = stringResource(R.string.about),
                            body = stringResource(id = R.string.about_body),
                            onDismiss = { showAboutDialog = false }
                        )
                }
            }
        }
    }
}

@Composable
fun CustomAlertDialog(
    title: String = stringResource(R.string.title),
    body: String = stringResource(R.string.body),
    onDismiss: () -> Unit = {}
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = { Text(body) },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                modifier = Modifier.padding(DP_8)
            ) {
                Text(stringResource(R.string.close))
            }
        }
    )
}

@Composable
fun MenuItem(
    icon: Painter,
    title: String,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = DP_8, horizontal = DP_20)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = icon,
            modifier = Modifier.size(DP_40),
            contentDescription = ""
        )
        Text(
            text = title,
            modifier = Modifier.padding(horizontal = DP_4),
        )
    }
    Divider(
        modifier = Modifier.padding(horizontal = DP_20),
        color = Color.LightGray,
        thickness = DP_1
    )

}

@Composable
fun ProfileElement(
    user: UserData?,
    state: ProfileState,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (state == ProfileState.LoggedIn) {
            AsyncImage(
                model = user?.imageUri,
                modifier = Modifier
                    .padding(DP_4)
                    .size(DP_100)
                    .border(border = BorderStroke(DP_1, Color.Black), shape = CircleShape)
                    .padding(DP_4)
                    .clip(CircleShape),
                contentDescription = ""
            )
            Text(
                text = stringResource(R.string.hi_user) + " ${user?.name}",
                modifier = Modifier.padding(DP_4)
            )
        } else {
            Box(
                modifier = Modifier
                    .requiredHeight(DP_50)
                    .border(BorderStroke(DP_1, Color.Black))
                    .padding(vertical = DP_4, horizontal = DP_20),
                contentAlignment = Alignment.Center
            ) {
                Text(text = stringResource(R.string.please_login))
            }
        }

    }
}