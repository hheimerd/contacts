package com.hheimerd.hangouts.components

import android.graphics.drawable.Icon
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
            .then(modifier),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        IconButton(onClick = onBackClick, modifier = Modifier.fillMaxHeight()) {
            Icon(backButtonIcon, stringResource(R.string.back))
        }
        Spacer(modifier = Modifier.topBarButtonSpace())
        Text(
            title,
            style = textStyle,
            modifier = Modifier.weight(1f), overflow = TextOverflow.Ellipsis
        )

        rightAngle()
    }
}