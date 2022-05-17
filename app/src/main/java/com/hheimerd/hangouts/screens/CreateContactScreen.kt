package com.hheimerd.hangouts.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddCircle
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.styles.paddingSm
import com.hheimerd.hangouts.styles.paddingXs
import com.hheimerd.hangouts.utils.typeUtils.Action
import com.hheimerd.hangouts.viewModels.ContactsViewModel

@Composable
fun CreateContactScreen(
    contactsViewModel: ContactsViewModel = viewModel(),
    onOpenSettingsClick: Action
) {
    CreateContactContent(onOpenSettingsClick = {})
}

@Composable
fun CreateContactContent(onOpenSettingsClick: Action = {}) {
    Scaffold(
        topBar = {
            CreateContactTopAppBar(onOpenSettingsClick = onOpenSettingsClick)
        },
        content = {
            Column(modifier = Modifier.padding(it)) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = paddingSm)
                ) {
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = stringResource(R.string.create_contact_add_photo),
                        Modifier
                            .size(50.dp)
                            .drawBehind {
                                drawCircle(Color.Cyan)
                            }
                            .padding(15.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(stringResource(id = R.string.create_contact_add_photo))
                }
            }
        }
    )
}

@Composable
fun CreateContactTopAppBar(
    modifier: Modifier = Modifier,
    onOpenSettingsClick: Action = {}
) {
    val menuExpanded = remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier
            .padding(5.dp)
            .then(modifier),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        Icon(
            Icons.Rounded.Close,
            contentDescription = stringResource(id = R.string.create_contact_title),
            modifier = Modifier.fillMaxHeight()
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = stringResource(id = R.string.create_contact_title),
            style = MaterialTheme.typography.h5,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(100),
            modifier = Modifier.width(85.dp)
        ) {
            Text(
                stringResource(id = R.string.create_contact_save),
            )
        }
        DropdownMenu(
            expanded = menuExpanded.value,
            onDismissRequest = { menuExpanded.value = false },
            offset = DpOffset((20).dp, (-40).dp),
            modifier = Modifier.width(170.dp)
        ) {
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
}

@Preview
@Composable
fun PreviewCreateContactScreen() {
    CreateContactContent()
}