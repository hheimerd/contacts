package com.hheimerd.hangouts.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun IconBefore(
    icon: ImageVector,
    modifier: Modifier = Modifier,
    iconSize: Dp = 25.dp,
    content: @Composable () -> Unit
) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier) {
        Icon(
            icon, "",
            Modifier
                .width(0.dp)
                .requiredSize(iconSize)
                .offset(x = (-30).dp)
        )

        content()
    }
}
