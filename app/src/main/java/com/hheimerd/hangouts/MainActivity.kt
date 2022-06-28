package com.hheimerd.hangouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.hheimerd.hangouts.ui.add_edit_contact.AddEditContactViewModel
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.ui.mainScreen.MainScreenViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null;

    fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Navigate -> navController?.navigate(event.routeString)
            UiEvent.PopBack -> navController?.popBackStack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)


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
                    composable("${Routes.Home}/?${contactIdParam}={${contactIdParam}}", contactIdParamArg) { navEntry ->
                        val viewModel: MainScreenViewModel = hiltViewModel()
                        LaunchedEffect(true) {
                            viewModel.uiEvent.collect(::onUiEvent)
                        }

                        MainScreen(viewModel)
                    }
                    composable(Routes.AddEditContact, contactIdParamArg) { navEntry ->
                        val viewModel: AddEditContactViewModel = hiltViewModel()
                        LaunchedEffect(true) {
                            viewModel.uiEvent.collect(::onUiEvent)
                        }
                        AddEditContactScreen(
                            viewModel,
                        )
                    }

                }
            }
        }
    }
}

