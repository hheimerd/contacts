package com.hheimerd.hangouts

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hheimerd.hangouts.components.ContactsListView
import com.hheimerd.hangouts.components.SearchTopAppBar
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.getRandomString
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.foundation.layout.*
import com.hheimerd.hangouts.screens.MainScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)


        setContent {
            val systemUiController = rememberSystemUiController()
            val darkTheme = isSystemInDarkTheme()

            SideEffect {
                if (darkTheme) {
                    systemUiController.setSystemBarsColor(Color.Transparent)
                } else {
                    systemUiController.setSystemBarsColor(Color.White)
                }
            }

            HangoutsTheme {
                MainScreen()
            }
        }
    }
}

