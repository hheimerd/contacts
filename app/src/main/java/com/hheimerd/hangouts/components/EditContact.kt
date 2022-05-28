package com.hheimerd.hangouts.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.styles.paddingSm
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.typeUtils.Action
import com.hheimerd.hangouts.utils.typeUtils.ActionWith


@Composable
fun EditContact(
    onSave: ActionWith<Contact>,
    initialValue: Contact = Contact("", ""),
    title: String,
    onOpenSettingsClick: Action,
    onClose: Action,
    modifier: Modifier = Modifier
) {

    var firstName: String by rememberSaveable { mutableStateOf(initialValue.firstName) }
    var lastName: String by rememberSaveable { mutableStateOf(initialValue.lastName) }
    var phone: String by rememberSaveable { mutableStateOf(initialValue.phone) }
    var email: String by rememberSaveable { mutableStateOf(initialValue.email) }
    var nickname: String by rememberSaveable { mutableStateOf(initialValue.nickname) }
    var imageUri: String? by rememberSaveable { mutableStateOf(initialValue.imageUri) }


    Scaffold(
        modifier = modifier,
        topBar = {
            CreateContactTopAppBar(
                onOpenSettingsClick = onOpenSettingsClick,
                title = title,
                onCloseClicked = onClose,
                onSaveClicked = {
                    onSave(
                        Contact(
                            phone,
                            firstName,
                            lastName,
                            email,
                            nickname,
                            imageUri
                        )
                    )
                }
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = paddingSm)
                ) {
                    val secondary = MaterialTheme.colors.secondary;
                    Icon(
                        Icons.Outlined.Add,
                        contentDescription = stringResource(R.string.add_photo),
                        Modifier
                            .size(50.dp)
                            .drawBehind {
                                drawCircle(secondary)
                            }
                            .padding(15.dp)
                    )
                    Spacer(modifier = Modifier.height(5.dp))
                    Text(stringResource(id = R.string.add_photo))
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    OutlinedTextFieldWithIcon(
                        value = firstName,
                        onValueChange = { value -> firstName = value },
                        placeholderText = stringResource(R.string.firstName),
                        Icons.Outlined.Person
                    )

                    OutlinedTextField(value = lastName,
                        onValueChange = { value -> lastName = value },
                        placeholder = { Text(stringResource(R.string.lastName)) }
                    )

                    OutlinedTextFieldWithIcon(
                        value = phone,
                        onValueChange = { value -> phone = value },
                        placeholderText = stringResource(R.string.phone),
                        icon = Icons.Outlined.Phone,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                    )

                    OutlinedTextFieldWithIcon(
                        value = email,
                        onValueChange = { value -> email = value },
                        placeholderText = stringResource(R.string.email),
                        icon = Icons.Outlined.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )

                    OutlinedTextFieldWithIcon(
                        value = nickname,
                        onValueChange = { value -> nickname = value },
                        placeholderText = stringResource(R.string.nickname),
                        icon = ImageVector.vectorResource(id = R.drawable.logo_42)
                    )
                }
            }
        }
    )
}

@Composable
fun OutlinedTextFieldWithIcon(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    icon: ImageVector,
    keyboardOptions: KeyboardOptions = KeyboardOptions()
) {
    Row {
        Icon(
            icon, "",
            Modifier
                .width(0.dp)
                .requiredSize(25.dp)
                .offset((-30).dp, 13.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholderText) },
            keyboardOptions = keyboardOptions
        )
    }
}

@Composable
fun CreateContactTopAppBar(
    title: String,
    onOpenSettingsClick: Action,
    onCloseClicked: Action,
    onSaveClicked: Action,
    modifier: Modifier = Modifier,
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
            contentDescription = stringResource(id = R.string.close),
            modifier = Modifier
                .fillMaxHeight()
                .clickable { onCloseClicked() }
        )
        Spacer(modifier = Modifier.width(20.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.h5,
            modifier = Modifier.weight(1f)
        )
        Button(
            onClick = onSaveClicked,
            shape = RoundedCornerShape(100),
            modifier = Modifier.width(85.dp)
        ) {
            Text(
                stringResource(id = R.string.save),
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
fun PreviewEditContactScreen() {
    HangoutsTheme(true) {
        EditContact(
            {},
            title = "Edit contact",
            onOpenSettingsClick = {},
            onClose = {},
        )
    }
}