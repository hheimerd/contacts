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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.compose.runtime.getValue;
import androidx.compose.runtime.setValue;
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.hheimerd.hangouts.navigation.Routes
import com.hheimerd.hangouts.screens.CreateContactScreen
import com.hheimerd.hangouts.screens.EditContactScreen
import com.hheimerd.hangouts.screens.MainScreen
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.viewModels.ContactsViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = isSystemInDarkTheme()
            val navController = rememberNavController()

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
                                .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
                        )
                ) {
                    composable(Routes.Home) {
                        val viewModel: ContactsViewModel = hiltViewModel()

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

                        val viewModel: ContactsViewModel = hiltViewModel()

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
                            val viewModel: ContactsViewModel = hiltViewModel()
                            EditContactScreen(
                                viewModel,
                                contactId = contactId,
                                onOpenSettingsClick = { navController.navigate(Routes.Settings) },
                                onUpdated = {
                                    navController.navigate("${Routes.Home}/?contactId=${it.id}")
                                },
                                onClose = { navController.popBackStack() },
                            )
                        }
                    }

                    composable(Routes.Create) {
                        val viewModel: ContactsViewModel = hiltViewModel()
                        CreateContactScreen(
                            viewModel,
                            onOpenSettingsClick = { navController.navigate(Routes.Settings) },
                            onCreated = {
                                navController.navigate("${Routes.Home}/?contactId=${it.id}")
                            },
                            onClose = { navController.popBackStack() },
                        );
                    }
                }
            }
        }
    }
}

