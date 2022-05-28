package com.hheimerd.hangouts.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.styles.transparent
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.typeUtils.Action
import com.hheimerd.hangouts.utils.typeUtils.ActionWith

@Composable
fun SearchTopAppBar(
    searchValue: String,
    onSearchChanged: ActionWith<String>,
    onAddContactClick: Action,
    onOpenSettingsClick: Action
) {
    val menuExpanded = remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp,
        modifier = Modifier.padding(5.dp)
    ) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .requiredHeight(45.dp)
                .padding(horizontal = 6.dp)
                .fillMaxWidth()
        ) {
            TextField(
                value = searchValue,
                textStyle = MaterialTheme.typography.body2,
                onValueChange = onSearchChanged,
                modifier = Modifier
                    .fillMaxSize()
                    .requiredHeight(50.dp),
                placeholder = {
                    Text(
                        stringResource(id = R.string.search_label),
                        style = MaterialTheme.typography.body2,
                        modifier = Modifier.fillMaxHeight()
                    )
                },
                colors = TextFieldDefaults.transparent,
                singleLine = true,
                visualTransformation = VisualTransformation.None,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = { focusManager.clearFocus() }),
                leadingIcon = {
                    Icon(
                        Icons.Rounded.Search,
                        contentDescription = stringResource(id = R.string.search)
                    )
                },
                trailingIcon = {
                    DropdownMenu(
                        expanded = menuExpanded.value,
                        onDismissRequest = { menuExpanded.value = false },
                        offset = DpOffset((20).dp, (-40).dp),
                        modifier = Modifier.width(170.dp)
                    ) {
                        DropdownMenuItem(onClick = {
                            menuExpanded.value = false; onAddContactClick()
                        }) {
                            Text(text = stringResource(id = R.string.add_contact))
                        }
                        DropdownMenuItem(onClick = {
                            menuExpanded.value = false; onOpenSettingsClick()
                        }) {
                            Text(text = stringResource(id = R.string.settings))
                        }
                    }
                    Icon(
                        Icons.Rounded.MoreVert,
                        stringResource(id = R.string.more),
                        modifier = Modifier.clickable { menuExpanded.value = !menuExpanded.value }
                    )
                }
            )
        }

    }
}


@Preview
@Composable
fun SearchPreview() {
    HangoutsTheme(true) {
        Scaffold(
            topBar = {
                SearchTopAppBar("", {}, {}, {})
            },
            backgroundColor = MaterialTheme.colors.background
        ) {it ->
            it
        }
    }
}
