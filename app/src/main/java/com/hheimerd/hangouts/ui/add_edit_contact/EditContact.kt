package com.hheimerd.hangouts.ui.add_edit_contact

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.components.Avatar
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.ui.styles.paddingSm
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.InternalStorage
import com.hheimerd.hangouts.utils.typeUtils.Action
import com.hheimerd.hangouts.utils.typeUtils.ActionWith
import java.util.*


@Composable
fun EditContact(
    onEvent: ActionWith<AddEditContactEvent>,
    initialValue: Contact,
    scaffoldState: ScaffoldState,
    title: String,
    modifier: Modifier = Modifier
) {

    var firstName: String by rememberSaveable { mutableStateOf(initialValue.firstName) }
    var lastName: String by rememberSaveable { mutableStateOf(initialValue.lastName) }
    var phone: String by rememberSaveable { mutableStateOf(initialValue.phone) }
    var email: String by rememberSaveable { mutableStateOf(initialValue.email) }
    var nickname: String by rememberSaveable { mutableStateOf(initialValue.nickname) }
    var imageUri: String? by rememberSaveable { mutableStateOf(initialValue.imageUri) }


    var showErrors: Boolean by rememberSaveable { mutableStateOf(false) }

    val context = LocalContext.current
    val focusManager = LocalFocusManager.current

    val cameraActivity = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(), onResult = { bitmap ->
            if (bitmap == null)
                return@rememberLauncherForActivityResult

            val uri = UUID.randomUUID().toString()
            if (InternalStorage.savePhoto(context, uri, bitmap)) {
                imageUri = uri
            }
        }
    )

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = modifier,
        topBar = {
            SaveTopAppBar(
                onOpenSettingsClick = {onEvent(AddEditContactEvent.OnSettingsClick)},
                title = title,
                onCloseClicked = {onEvent(AddEditContactEvent.OnCloseButtonClick)},
                onSaveClicked = {
                    onEvent(AddEditContactEvent.OnSave(
                        Contact(phone, firstName, lastName, email, nickname, imageUri)
                    ))
                    showErrors = true
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
                        .clickable {
                            cameraActivity.launch(null)
                        }
                ) {
                    val secondary = MaterialTheme.colors.secondary;
                    if (imageUri != null) {
                        Avatar(imageUri = imageUri!!, modifier = Modifier.size(50.dp))
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
                    Text(stringResource(id = if(imageUri == null) R.string.add_photo else R.string.change_photo))
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    IconBefore(
                        icon = Icons.Outlined.Person
                    ) {
                        OutlinedTextField(
                            value = firstName,
                            onValueChange = { value -> firstName = value },
                            label = { Text(stringResource(R.string.firstName)) },
                            singleLine = true,
                            isError = showErrors && firstName.isEmpty(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                        )
                    }

                    OutlinedTextField(value = lastName,
                        onValueChange = { value -> lastName = value },
                        label = { Text(stringResource(R.string.lastName)) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),

                    )

                    IconBefore(
                        icon = Icons.Outlined.Phone

                    ) {
                        OutlinedTextField(
                            value = phone,
                            onValueChange = { value -> phone = value },
                            label = { Text(stringResource(R.string.phone)) },
                            singleLine = true,
                            isError = showErrors && phone.isEmpty(),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Phone),
                        )
                    }

                    IconBefore(
                        icon = Icons.Outlined.Email
                    ) {
                        OutlinedTextField(
                            value = email,
                            onValueChange = { value -> email = value },
                            label = { Text(stringResource(R.string.email)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),

                        )
                    }

                    IconBefore(
                        icon = ImageVector.vectorResource(id = R.drawable.logo_42)
                    ) {
                        OutlinedTextField(
                            value = nickname,
                            onValueChange = { value -> nickname = value },
                            label = { Text(stringResource(R.string.nickname)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                focusManager.clearFocus()
                            })
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun IconBefore(
    icon: ImageVector,
    content: @Composable () -> Unit
) {
    Row {
        Icon(
            icon, "",
            Modifier
                .width(0.dp)
                .requiredSize(25.dp)
                .offset((-30).dp, 25.dp)
        )

        content()
    }
}

@Composable
fun SaveTopAppBar(
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
            initialValue = Contact("", ""),
            rememberScaffoldState(),
            title = "Edit contact",
        )
    }
}