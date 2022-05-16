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
    Color(0xFFFED6BC),
    Color(0xFFFFFADD),
    Color(0xFFDEF7FE),
    Color(0xFFE7ECFF),
    Color(0xFFC3FBD8),
    Color(0xFFFDEED9),
    Color(0xFFF6FFF8),
    Color(0xFFB5F2EA),
    Color(0xFFC6D8FF),
)

fun getColorByInt(index: Int): Color {
    return colors[index.absoluteValue % colors.size]
}