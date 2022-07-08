package com.hheimerd.hangouts.ui.chat

import android.Manifest
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.components.DotsDropdownMenu
import com.hheimerd.hangouts.data.models.ChatMessage
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.ui.styles.topAppBarPadding
import com.hheimerd.hangouts.ui.styles.topBarButtonSpace
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.typeUtils.ActionWith

@Composable
fun ChatView(
    chatMessages: List<ChatMessage>,
    contact: Contact,
    onChatEvent: ActionWith<ChatEvent>,
    modifier: Modifier = Modifier
) {
    var messageText by remember { mutableStateOf("") }
    var sendMessageAllowed by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val requestSmsPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { allowed ->
        sendMessageAllowed = allowed
    }

    LaunchedEffect(true) {
        requestSmsPermission.launch(Manifest.permission.SEND_SMS)
    }

    Scaffold(
        topBar = { ChatAppBar(title = contact.fullName, onChatEvent) },
        modifier = Modifier.then(modifier)
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(chatMessages) { chatMessage: ChatMessage ->
                    Message(chatMessage)
                }
            }

            TextField(
                value = messageText,
                enabled = sendMessageAllowed,
                onValueChange = { messageText = it },
                shape = RoundedCornerShape(5.dp),
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { onChatEvent(ChatEvent.SendMessage(messageText, context)) }) {
                        Icon(Icons.Outlined.Send, stringResource(R.string.send))
                    }
                }
            )
        }
    }
}

@Composable
fun Message(chatMessage: ChatMessage) {
    Text(chatMessage.text)
}

@Composable
fun ChatAppBar(
    title: String,
    onChatEvent: ActionWith<ChatEvent>,
) {
    val menuExpanded = remember { mutableStateOf(false) }

    TopAppBar(
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .topAppBarPadding(),
        elevation = 0.dp
    ) {
        IconButton(onClick = { onChatEvent(ChatEvent.GoBack) }) {
            Icon(
                Icons.Rounded.ArrowBack,
                contentDescription = stringResource(id = R.string.close),
                modifier = Modifier
                    .fillMaxHeight()
            )
        }

        Spacer(modifier = Modifier.topBarButtonSpace())

        Text(title, overflow = TextOverflow.Ellipsis, modifier = Modifier.fillMaxWidth())

        DotsDropdownMenu(menuExpanded) {
            DropdownMenuItem(onClick = {
                menuExpanded.value = false;
                onChatEvent(ChatEvent.RemoveChat)
            }) {
                Text(text = stringResource(id = R.string.delete))
            }
        }
    }
}

@Composable
@Preview
fun ChatViewPreview() {
    HangoutsTheme(true) {
        ChatView(
            listOf(),
            Contact("1234", "Test"),
            {}
        )
    }
}
