package com.hheimerd.hangouts.components

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.hheimerd.hangouts.R

@Composable
fun DotsDropdownMenu(
    menuExpanded: MutableState<Boolean>,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    IconButton(onClick = { menuExpanded.value = !menuExpanded.value }) {
        Icon(
            Icons.Rounded.MoreVert,
            stringResource(id = R.string.more),
        )

        DropdownMenu(
            expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false },
            offset = DpOffset(0.dp, 0.dp),
            modifier = Modifier.width(170.dp),
            content = content
        )
    }
}