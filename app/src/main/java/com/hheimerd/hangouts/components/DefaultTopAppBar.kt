package com.hheimerd.hangouts.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.ui.styles.topAppBarPadding
import com.hheimerd.hangouts.ui.styles.topBarButtonSpace
import com.hheimerd.hangouts.utils.typeUtils.Action

@Composable
fun DefaultTopAppBar(
    title: String,
    onBackClick: Action,
    modifier: Modifier = Modifier,
    backButtonIcon: ImageVector = Icons.Outlined.ArrowBack,
    textStyle: TextStyle = MaterialTheme.typography.h5,
    rightAngle: @Composable RowScope.() -> Unit = {}
) {
    TopAppBar(
        modifier
            .topAppBarPadding()
            .fillMaxWidth()
            .then(modifier),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        IconButton(onClick = onBackClick, modifier = Modifier.fillMaxHeight()) {
            Icon(backButtonIcon, stringResource(R.string.back))
        }
        Text(
            title,
            style = textStyle,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .weight(1f)
        )

        rightAngle()
    }
}