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
import com.hheimerd.hangouts.screens.CreateContactScreen
import com.hheimerd.hangouts.screens.EditContactScreen
import com.hheimerd.hangouts.screens.MainScreen
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.viewModels.ContactsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var navController: NavHostController? = null;

    fun onUiEvent(event: UiEvent) {
        when (event) {
            is UiEvent.Navigate -> navController?.navigate(event.routeString)
            UiEvent.PopBack -> navController?.popBackStack()
            is UiEvent.ShowSnackbar -> TODO()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = isSystemInDarkTheme()
            val navController = rememberNavController()
            val viewModel: ContactsViewModel = hiltViewModel()

            this.navController = navController;

            SideEffect {
                if (darkTheme) {
                    systemUiController.setSystemBarsColor(Color.Transparent)
                } else {
                    systemUiController.setSystemBarsColor(Color.White)
                }
            }


            LaunchedEffect(true) {
                viewModel.uiEvent.collect(::onUiEvent)
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
                                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                        )
                ) {
                    composable(Routes.Home) {

                        MainScreen(
                            viewModel,
                            onOpenSettingsClick = { navController.navigate(Routes.Settings) })
                    }
                    composable("${Routes.Home}/?contactId={contactId}", arguments = listOf(
                        navArgument("contactId") {
                            type = NavType.StringType;
                            nullable = true
                        }
                    )) { navEntry ->
                        val contactId = navEntry.arguments?.getString("contactId")

                        MainScreen(
                            viewModel,
                            onOpenSettingsClick = { navController.navigate(Routes.Settings) },
                            initialContactId = contactId)
                    }
                    composable("${Routes.Edit}/{contactId}") { navEntry ->
                        val contactId = navEntry.arguments?.getString("contactId")
                        if (contactId == null)
                            navController.navigate(Routes.Home)
                        else {
                            EditContactScreen(
                                viewModel,
                                contactId = contactId,
                                onUiEvent = ::onUiEvent
                            )
                        }
                    }

                    composable(Routes.Create) {
                        CreateContactScreen(
                            viewModel,
                            onUiEvent = ::onUiEvent
                        );
                    }
                }
            }
        }
    }
}

