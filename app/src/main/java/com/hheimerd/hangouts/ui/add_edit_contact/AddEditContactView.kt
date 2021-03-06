package com.hheimerd.hangouts.ui.add_edit_contact

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.components.Avatar
import com.hheimerd.hangouts.components.DefaultTopAppBar
import com.hheimerd.hangouts.components.IconBefore
import com.hheimerd.hangouts.ui.StringResource
import com.hheimerd.hangouts.ui.styles.paddingSm
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.InternalStorage
import com.hheimerd.hangouts.utils.typeUtils.ActionWith
import java.util.*


@Composable
fun AddEditContactView(
    onEvent: ActionWith<AddEditContactEvent>,
    contactState: AddEditContactStateHost,
    scaffoldState: ScaffoldState,
    title: String,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val cameraActivity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(), onResult = { bitmap ->
            if (bitmap == null)
                return@rememberLauncherForActivityResult

            val uri = UUID.randomUUID().toString()
            if (InternalStorage.savePhoto(context, uri, bitmap)) {
                contactState.imageUri.value = uri
            }
        }
    )

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier,
        topBar = {
            AddEditTopAppBar(
                title = title,
                onEvent,
                contactState
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .verticalScroll(rememberScrollState())
            ) {

                // Load Image
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = paddingSm)
                        .clickable {
                            cameraActivity.launch(null)
                        }
                ) {
                    val secondary = MaterialTheme.colors.secondary;
                    if (contactState.imageUri.value.isNullOrBlank() == false) {
                        Avatar(
                            imageUri = contactState.imageUri.value,
                            modifier = Modifier.size(50.dp)
                        )
                    } else {
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
                    }

                    Spacer(modifier = Modifier.height(5.dp))
                    Text(
                        stringResource(
                            if (contactState.imageUri.value.isBlank()) R.string.add_photo
                            else R.string.change_photo
                        )
                    )
                }

                // Fields
                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    // First name
                    Field(
                        valueState = contactState.firstName,
                        label = stringResource(R.string.firstName),
                        error = contactState.firstNameError.value?.asString(),
                        icon = Icons.Outlined.Person
                    )

                    // Last name
                    Field(
                        valueState = contactState.lastName,
                        label = stringResource(R.string.lastName),
                        error = contactState.lastNameError.value?.asString(),
                    )

                    // Phone
                    Field(
                        valueState = contactState.phone,
                        label = stringResource(R.string.phone),
                        keyboardType = KeyboardType.Phone,
                        error = contactState.phoneError.value?.asString(),
                        icon = Icons.Outlined.Phone
                    )

                    // Email
                    Field(
                        valueState = contactState.email,
                        label = stringResource(R.string.email),
                        keyboardType = KeyboardType.Email,
                        error = contactState.emailError.value?.asString(),
                        icon = Icons.Outlined.Email
                    )

                    // Nick
                    Field(
                        valueState = contactState.nickname,
                        label = stringResource(R.string.nickname),
                        keyboardType = KeyboardType.Email,
                        error = contactState.nicknameError.value?.asString(),
                        icon = Icons.Outlined.Email,
                        imeAction = ImeAction.Done,
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus()
                        })
                    )

                }
            }
        }
    )
}

@Composable
fun Field(
    valueState: MutableState<String>,
    error: String? = null,
    label: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    icon: ImageVector? = null,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions()
) {
    val content = @Composable {
        Column {
            OutlinedTextField(
                value = valueState.value,
                onValueChange = { newValue -> valueState.value = newValue },
                label = { Text(label) },
                isError = error != null,
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = keyboardType,
                    imeAction = imeAction
                ),
                keyboardActions = keyboardActions,
                maxLines = 1
            )
            if (error != null)
                Text(error, color = Color.Red, fontSize = 3.em)
        }
    }
    if (icon == null)
        content()
    else {
        IconBefore(
            icon = icon
        ) {
            content()
        }
    }

}

@Composable
fun AddEditTopAppBar(
    title: String,
    onEvent: ActionWith<AddEditContactEvent>,
    contactState: AddEditContactStateHost,
    modifier: Modifier = Modifier,
) {
    val menuExpanded = remember { mutableStateOf(false) }

    DefaultTopAppBar(
        title,
        onBackClick = { onEvent(AddEditContactEvent.OnCloseButtonClick) },
        backButtonIcon = Icons.Rounded.Close
    ) {
        Button(
            onClick = { onEvent(AddEditContactEvent.OnSave(contactState)) },
            shape = RoundedCornerShape(100),

        ) {
            Text(
                stringResource(id = R.string.save),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }

        IconButton(onClick = { menuExpanded.value = !menuExpanded.value }) {
            Icon(
                Icons.Rounded.MoreVert,
                stringResource(id = R.string.more),
            )

            DropdownMenu(
                expanded = menuExpanded.value,
                onDismissRequest = { menuExpanded.value = false },
                offset = DpOffset(0.dp, 0.dp),
                modifier = Modifier.width(170.dp)
            ) {
                DropdownMenuItem(onClick = {
                    menuExpanded.value = false;
                    onEvent(AddEditContactEvent.OnSettingsClick)
                }) {
                    Text(text = stringResource(id = R.string.settings))
                }
            }
        }

    }
}

@Preview
@Composable
fun PreviewEditContactScreen() {
    HangoutsTheme(true) {
        AddEditContactView(
            {},
            contactState = with(AddEditContactStateHost()) {
                phoneError.value = StringResource(R.string.phone_cant_be_empty)
                this
            },
            rememberScaffoldState(),
            title = "Edit contact",
        )
    }
}
