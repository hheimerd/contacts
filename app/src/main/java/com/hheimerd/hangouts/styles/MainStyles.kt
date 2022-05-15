package com.hheimerd.hangouts.styles

import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val TextFieldDefaults.transparent
    @Composable get()= TextFieldDefaults.textFieldColors(
    backgroundColor = Color.Transparent,
    disabledTextColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent
);