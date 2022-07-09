package com.hheimerd.hangouts.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.components.DefaultTopAppBar
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.typeUtils.Action
import com.hheimerd.hangouts.utils.typeUtils.ActionWith

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
    Color.Transparent,
)

@Composable
fun SettingsView(onBackClick: Action, onHeaderColorChanged: ActionWith<Color>) {
    Scaffold(
        topBar = {
            DefaultTopAppBar(
                title = stringResource(R.string.settings),
                onBackClick = onBackClick
            )
        }
    ) {
        Column(modifier = Modifier.padding(it).fillMaxSize()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 25.dp, vertical = 10.dp)
            ) {
                Text(stringResource(R.string.header_color), fontSize = 5.em)
                Button(onClick = { onHeaderColorChanged(colors.random()) }) {
                    Text(stringResource(R.string.random_color))
                }
            }
        }
    }
}

@Composable
@Preview
fun SettingsPreview() {
    HangoutsTheme {
        SettingsView({}, {})
    }
}
