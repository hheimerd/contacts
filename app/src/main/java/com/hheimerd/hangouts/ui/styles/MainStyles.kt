package com.hheimerd.hangouts.ui.styles

import androidx.compose.foundation.layout.*
import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

val TextFieldDefaults.transparent
    @Composable get()= TextFieldDefaults.textFieldColors(
    backgroundColor = Color.Transparent,
    disabledTextColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent
);

val paddingXs = 8.dp
val paddingSm = 16.dp
val paddingMd = 24.dp
val paddingLg = 32.dp

fun Modifier.avatarSmallSize(): Modifier {
    return this
        .size(40.dp)
        .requiredSize(40.dp)
}

fun Modifier.topAppBarPadding(): Modifier {
    return this.padding(5.dp)
}


fun Modifier.avatarSpace(): Modifier {
    return this.width(14.dp)
}

fun Modifier.topBarButtonSpace(): Modifier {
    return this.width(20.dp)
}