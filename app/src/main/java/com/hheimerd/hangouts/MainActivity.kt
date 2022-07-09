package com.hheimerd.hangouts

import android.Manifest
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hheimerd.hangouts.events.UiEvent
import com.hheimerd.hangouts.navigation.Routes
import com.hheimerd.hangouts.navigation.Routes.Companion.contactIdParam
import com.hheimerd.hangouts.ui.add_edit_contact.AddEditContactScreen
import com.hheimerd.hangouts.screens.MainScreen
import com.hheimerd.hangouts.screens.SettingsScreen
import com.hheimerd.hangouts.ui.add_edit_contact.AddEditContactViewModel
import com.hheimerd.hangouts.ui.chat.ChatScreen
import com.hheimerd.hangouts.ui.chat.ChatViewModel
import com.hheimerd.hangouts.ui.contact_card.ContactCardScreen
import com.hheimerd.hangouts.ui.contact_card.ContactCardViewModel
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.ui.main_screen.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null;
    private var hideDate: Date? = null

    fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Navigate -> {
                navController?.navigate(event.routeString) {
                    if (event.withPopAll)
                        popUpTo(event.routeString)
                }
            }
            UiEvent.PopBack -> navController?.popBackStack()
            else -> {}
        }
    }

    override fun onStart() {
        super.onStart()
        if (hideDate != null)
            Toast.makeText(this, hideDate.toString(), Toast.LENGTH_LONG).show()
    }

    override fun onStop() {
        super.onStop()
        hideDate = Date()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val requestSmsSend = registerForActivityResult(ActivityResultContracts.RequestPermission()) { allowed ->
            if (!allowed) {
                Toast.makeText(this, getString(R.string.SMS_send_permisson_required), Toast.LENGTH_LONG).show()
            }
        }

        val requestSmsReceive = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}

        requestSmsSend.launch(Manifest.permission.SEND_SMS)
        requestSmsReceive.launch(Manifest.permission.RECEIVE_SMS)
        requestSmsReceive.launch(Manifest.permission.READ_SMS)


        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = isSystemInDarkTheme()
            val navController = rememberNavController()

            this.navController = navController

            val contactIdParamArg = listOf(
                navArgument(contactIdParam) {
                    type = NavType.StringType;
                    nullable = true
                }
            )

            SideEffect {
                if (darkTheme) {
                    systemUiController.setSystemBarsColor(Color.Transparent)
                } else {
                    systemUiController.setSystemBarsColor(Color.White)
                }
            }


            HangoutsTheme {
                NavHost(
                    navController = navController,
                    startDestination = Routes.Home,
                    modifier = Modifier
                        .background(MaterialTheme.colors.background)
                        .fillMaxSize()
                        .statusBarsPadding()
                        .windowInsetsPadding(
                            WindowInsets
                                .navigationBars
                        )
                ) {
                    composable(Routes.Home) {
                        val viewModel: MainScreenViewModel = hiltViewModel()
                        LaunchedEffect(true) {
                            viewModel.uiEvent.collect(::onUiEvent)
                        }
                        MainScreen(viewModel)
                    }
                    composable(Routes.ContactCard, contactIdParamArg) {
                        val viewModel: ContactCardViewModel = hiltViewModel()
                        LaunchedEffect(true) {
                            viewModel.uiEvent.collect(::onUiEvent)
                        }

                        ContactCardScreen(viewModel)
                    }
                    composable(Routes.AddEditContact, contactIdParamArg) {
                        val viewModel: AddEditContactViewModel = hiltViewModel()
                        LaunchedEffect(true) {
                            viewModel.uiEvent.collect(::onUiEvent)
                        }
                        AddEditContactScreen(
                            viewModel,
                        )
                    }
                    composable(Routes.Chat, contactIdParamArg) {
                        val viewModel: ChatViewModel = hiltViewModel()
                        LaunchedEffect(true) {
                            viewModel.uiEvent.collect(::onUiEvent)
                        }
                        ChatScreen(
                            viewModel
                        )
                    }
                    composable(Routes.Settings) {
                        SettingsScreen(systemUiController, onPopBack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}

