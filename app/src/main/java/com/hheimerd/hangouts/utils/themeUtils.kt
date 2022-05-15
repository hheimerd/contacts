package com.hheimerd.hangouts.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import kotlin.math.absoluteValue
import kotlin.random.Random

@Composable
fun rememberColorByString(string: String): Color {
    return remember(string) {
        getColorByInt(string.hashCode())
    }
}

fun Color.Companion.random(seed: Any? = null): Color {
    val hash = seed?.hashCode() ?: Random.nextInt().hashCode()
    return getColorByInt(hash)
}

val colors = listOf(
    Color.Cyan,
    Color.Gray,
    Color.LightGray,
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Yellow,
    Color.Magenta
)

fun getColorByInt(index: Int): Color {
    return colors[index.absoluteValue % colors.size]
}