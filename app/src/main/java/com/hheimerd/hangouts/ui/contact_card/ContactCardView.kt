package com.hheimerd.hangouts.ui.contact_card

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.components.Avatar
import com.hheimerd.hangouts.components.AvatarDefault
import com.hheimerd.hangouts.components.IconBefore
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.ui.add_edit_contact.AddEditContactView
import com.hheimerd.hangouts.ui.styles.topAppBarPadding
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.InternalStorage
import com.hheimerd.hangouts.utils.rememberColorByString
import com.hheimerd.hangouts.utils.typeUtils.ActionWith

@Composable
fun ContactCardView(
    contact: Contact,
    onEvent: ActionWith<ContactCardEvent>
) {
    val context = LocalContext.current

    Scaffold(
        topBar = { ContactCardTopBar(onEvent) }
    ) { scaffoldPaddings ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPaddings)
        ) {
            val avatarBitmap =
                if (contact.imageUri != null) InternalStorage.getPhoto(context, contact.imageUri)
                else null

            if (avatarBitmap == null) {
                val color = rememberColorByString(contact.firstName)
                AvatarDefault(
                    letter = contact.firstName.first(),
                    color,
                    Modifier.size(60.dp),
                    fontSize = 8.em
                )
            } else {
                Image(
                    bitmap = avatarBitmap.asImageBitmap(),
                    "",
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .height(230.dp)
                        .fillMaxWidth()
                );
            }

            Text(
                text = "${contact.firstName} ${contact.lastName}",
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                fontSize = 6.em,
                modifier = Modifier.padding(vertical = 20.dp)
            )

            Divider(
                Modifier
                    .fillMaxWidth()
                    .height(1.5.dp)
            )
            ActionButtons(
                onEvent, modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
            )
            Divider(
                Modifier
                    .fillMaxWidth()
                    .height(1.5.dp)
            )

            Surface(
                elevation = 5.dp,
                modifier = Modifier.padding(20.dp),
                shape = RoundedCornerShape(5)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 60.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    if (contact.email.isNotBlank()) {
                        IconBefore(
                            Icons.Outlined.Email,
                            iconSize = 30.dp,
                        ) {
                            Text(
                                text = contact.email,
                                fontSize = 4.em,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    if (contact.phone.isNotBlank()) {
                        IconBefore(
                            Icons.Outlined.Phone,
                            iconSize = 30.dp,
                        ) {
                            Text(
                                text = contact.phone,
                                fontSize = 4.em,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    if (contact.nickname.isNotBlank()) {
                        IconBefore(
                            ImageVector.vectorResource(id = R.drawable.logo_42),
                            iconSize = 30.dp,
                        ) {
                            Text(
                                text = contact.nickname,
                                fontSize = 4.em,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }
            }


        }
    }

}

@Composable
fun ActionButtons(onEvent: ActionWith<ContactCardEvent>, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.then(modifier)
    ) {
        Button(
            onClick = { onEvent(ContactCardEvent.Call) },
            elevation = ButtonDefaults.elevation(0.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            modifier = Modifier.width(120.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(Icons.Outlined.Phone, stringResource(R.string.call))
                Text(stringResource(R.string.call))
            }
        }

        Button(
            onClick = { onEvent(ContactCardEvent.TextMessage) },
            elevation = ButtonDefaults.elevation(0.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            modifier = Modifier.width(120.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    ImageVector.vectorResource(R.drawable.message),
                    stringResource(R.string.textMessage)
                )
                Text(stringResource(R.string.textMessage))
            }
        }
    }
}

@Composable
@Preview
fun ButtonsPreview() {
    HangoutsTheme {
        ActionButtons({}, Modifier)
    }
}

@Composable
@Preview
fun CardPreview() {
    HangoutsTheme(true) {
        ContactCardView(contact = Contact(
            "1234",
            "Firstname",
            email = "email",
            nickname = "hheimerd",
            lastName = "Lastname",
        ), onEvent = {})
    }
}


@Composable
@Preview
fun CardPreviewWithAvatar() {
    HangoutsTheme(true) {
        ContactCardView(contact = Contact(
            "1234",
            "Firstname",
            email = "email",
            nickname = "hheimerd",
            lastName = "Lastname",
            imageUri = "test"
        ), onEvent = {})
    }
}

@Composable
fun ContactCardTopBar(onEvent: ActionWith<ContactCardEvent>) {
    var navExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier
            .topAppBarPadding(),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        // Back
        IconButton(onClick = { onEvent(ContactCardEvent.BackButtonClicked) }) {
            Icon(Icons.Outlined.ArrowBack, stringResource(R.string.back))
        }
        Spacer(Modifier.weight(1f))

        // Edit
        IconButton(onClick = { onEvent(ContactCardEvent.EditContactClick) }) {
            Icon(Icons.Outlined.Edit, stringResource(R.string.edit))
        }

        IconButton(onClick = { navExpanded = !navExpanded }) {
            Icon(Icons.Outlined.MoreVert, stringResource(R.string.more))
            DropdownMenu(
                expanded = navExpanded,
                onDismissRequest = { navExpanded = false },
                offset = DpOffset(0.dp, 0.dp)
            ) {
                DropdownMenuItem(onClick = { onEvent(ContactCardEvent.DeleteContactClick) }) {
                    Text(stringResource(R.string.delete))
                }
            }
        }
    }
}