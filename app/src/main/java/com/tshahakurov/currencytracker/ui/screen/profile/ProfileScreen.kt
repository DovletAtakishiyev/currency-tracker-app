package com.tshahakurov.currencytracker.ui.screen.profile

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.tshahakurov.currencytracker.R
import com.tshahakurov.currencytracker.app.logic.auth.GoogleAuthUiClient
import com.tshahakurov.currencytracker.data.model.UserData
import com.tshahakurov.currencytracker.ui.navigation.AppBar
import com.tshahakurov.currencytracker.ui.navigation.CurrencyScreens
import com.tshahakurov.currencytracker.ui.theme.DP_100
import com.tshahakurov.currencytracker.ui.theme.DP_150
import com.tshahakurov.currencytracker.ui.theme.DP_20
import com.tshahakurov.currencytracker.ui.theme.DP_4
import com.tshahakurov.currencytracker.data.model.converter.toUserData
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    user: UserData = UserData.defaultUser,
    viewModel: ProfileViewModel = hiltViewModel(),
    onBackPressed: () -> Unit = {},
    onLoginUser: (user:UserData) -> Unit = {},
    onRemoveUser: () -> Unit = {},
) {
    val state by viewModel.screenState.collectAsState()
    val isConnected by viewModel.isNetworkConnected.collectAsState(initial = false)
    val message = stringResource(id = R.string.no_connection)
    viewModel.startObservingNetworkState()
    viewModel.checkLogin(user)

//----------------------------------- Google -----------------------------------\\
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = context,
            oneTapClient = Identity.getSignInClient(context)
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == RESULT_OK) {
                scope.launch {
                    val signInResult = googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult, onLoginUser)
                }
            }
        }
    )
//----------------------------------- Google -----------------------------------//

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppBar(CurrencyScreens.Profile, onBackPressed = onBackPressed)
            val msg = stringResource(R.string.sing_out)
            when (state) {
                is ScreenState.LoggedIn -> LoggedInElement(Firebase.auth.toUserData()) {
                    scope.launch {
                        googleAuthUiClient.signOut()
                        Toast.makeText(
                            context,
                            msg,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    viewModel.loginOut()
                    onRemoveUser()
                }

                is ScreenState.Loading -> LoadingElement()
                else -> NotLoggedInElement {
                    if (isConnected){
//----------------------------------- Google -----------------------------------\\
                    scope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
//----------------------------------- Google -----------------------------------//
                     } else {
                        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}


@Composable
fun LoadingElement() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(imageVector = Icons.Default.Star, contentDescription = "")
    }
}

@Composable
fun NotLoggedInElement(
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = stringResource(R.string.not_logged))
            Spacer(modifier = Modifier.height(DP_20))
            Button(onClick = onClick) {
                Text(text = stringResource(R.string.google_login))
            }
        }
    }
}

@Composable
fun LoggedInElement(
    user: UserData,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = user.imageUri,
                modifier = Modifier
                    .size(DP_150)
                    .clip(CircleShape)
                    .border(border = BorderStroke(DP_4, Color.Magenta), shape = CircleShape)
                    .padding(DP_4),
                contentDescription = ""
            )
            Text(
                modifier = Modifier.padding(vertical = DP_20),
                text = user.name ?: stringResource(R.string.n_a)
            )
            Text(
                modifier = Modifier.padding(vertical = DP_20),
                text = user.email
            )
            Button(
                modifier = Modifier.padding(top = DP_20, bottom = DP_100),
                onClick = onClick
            ) {
                Text(text = stringResource(R.string.log_out))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    ProfileScreen()
}