package com.hheimerd.hangouts.screens

import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.SystemUiController
import com.hheimerd.hangouts.ui.settings.SettingsView
import com.hheimerd.hangouts.utils.typeUtils.Action

@Composable
fun SettingsScreen(systemUiController: SystemUiController, onPopBack: Action) {
    SettingsView(
        onBackClick = onPopBack,
        onHeaderColorChanged = { newColor ->
            systemUiController.setSystemBarsColor(newColor)
        }
    )
}