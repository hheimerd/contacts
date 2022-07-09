package com.hheimerd.hangouts.ui.chat

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.components.Avatar
import com.hheimerd.hangouts.components.AvatarDefault
import com.hheimerd.hangouts.components.DefaultTopAppBar
import com.hheimerd.hangouts.components.DotsDropdownMenu
import com.hheimerd.hangouts.data.models.ChatMessage
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.data.models.MessageDirection
import com.hheimerd.hangouts.ui.styles.transparent
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.rememberColorByString
import com.hheimerd.hangouts.utils.typeUtils.Action
import com.hheimerd.hangouts.utils.typeUtils.ActionWith

@Composable
fun ChatView(
    chatMessages: List<ChatMessage>,
    contact: Contact,
    onChatEvent: ActionWith<ChatEvent>,
    sendMessageAllowed: Boolean,
    modifier: Modifier = Modifier
) {
    val messageText = remember { mutableStateOf("") }
    val lazyListSTate = rememberLazyListState()

    val context = LocalContext.current

    val requestSmsPermission = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { allowed ->
        onChatEvent(ChatEvent.SendSmsPermissionResult(allowed))
        if (!allowed) {
            Toast.makeText(context, R.string.SMS_send_permisson_required, Toast.LENGTH_LONG).show()
        }
    }

    LaunchedEffect(true) {
        requestSmsPermission.launch(Manifest.permission.SEND_SMS)
    }

    Scaffold(
        topBar = { ChatAppBar(title = contact.fullName, onChatEvent) },
        modifier = Modifier.then(modifier),
    ) { scaffoldPadding ->
        Column(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize(),
        ) {
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Bottom,
                state = lazyListSTate,
                reverseLayout = true
            ) {
                items(chatMessages.reversed(), key = { it.id }) { chatMessage: ChatMessage ->
                    when (chatMessage.messageDirection) {
                        MessageDirection.Incoming -> MessageIncoming(chatMessage, contact)
                        MessageDirection.Outgoing -> MessageOutgoing(chatMessage)
                    }
                }
            }

            MessageField(
                sendMessageAllowed = sendMessageAllowed,
                messageText = messageText,
                onSend = {
                    onChatEvent(
                        ChatEvent.SendMessage(
                            messageText.value,
                            context
                        )
                    )
                }
            )
        }
    }
}

@Composable
fun MessageIncoming(chatMessage: ChatMessage, contact: Contact, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .padding(end = 40.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        if (contact.imageUri.isNullOrEmpty())
            AvatarDefault(
                contact.firstName.first(),
                color = rememberColorByString(contact.fullName),
                modifier = Modifier.padding(bottom = 4.dp)
            )
        else
            Avatar(contact.imageUri, modifier = Modifier.padding(bottom = 4.dp))
        MessageText(text = chatMessage.text)
    }

}

@Composable
fun MessageOutgoing(chatMessage: ChatMessage, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .then(modifier)
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp)
            .padding(start = 40.dp),
        horizontalArrangement = Arrangement.End
    ) {
        MessageText(
            text = chatMessage.text,
            textAlign = TextAlign.Right,
            surfaceColor = MaterialTheme.colors.primary.copy(alpha = 0.87f)
        )
    }
}

@Composable
fun MessageField(
    sendMessageAllowed: Boolean,
    messageText: MutableState<String>,
    onSend: Action
) {
    Row(verticalAlignment = Alignment.Bottom) {
        Card(
            elevation = 4.dp,
            shape = RoundedCornerShape(40.dp),
            modifier = Modifier
                .padding(10.dp)
                .weight(1f)
        ) {
            TextField(
                value = messageText.value,
                enabled = sendMessageAllowed,
                onValueChange = { messageText.value = it },
                placeholder = {
                    if (sendMessageAllowed)
                        Text(text = stringResource(R.string.message))
                    else
                        Text(stringResource(R.string.SMS_send_not_allowed))
                },
                colors = TextFieldDefaults.transparent,
                modifier = Modifier
                    .fillMaxWidth(),
                maxLines = 4
            )

        }
        IconButton(
            enabled = sendMessageAllowed,
            onClick = onSend,
            modifier = Modifier
                .padding(end = 10.dp, bottom = 13.dp)

        ) {
            Icon(
                Icons.Outlined.Send,
                stringResource(R.string.send)
            )
        }
    }
}

@Composable
fun MessageText(
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign? = null,
    surfaceColor: Color = MaterialTheme.colors.surface
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.padding(horizontal = 5.dp),
        elevation = 3.dp,
        color = surfaceColor,
    ) {
        Text(
            text, modifier = Modifier
                .padding(10.dp)
                .then(modifier),
            textAlign = textAlign
        )
    }
}

@Composable
fun ChatAppBar(
    title: String,
    onChatEvent: ActionWith<ChatEvent>,
) {
    val menuExpanded = remember { mutableStateOf(false) }

    DefaultTopAppBar(title, onBackClick = { onChatEvent(ChatEvent.GoBack) }, backButtonIcon = Icons.Rounded.ArrowBack) {
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
    val from = ChatMessage(
        "Sample text from",
        contactId = "",
        messageDirection = MessageDirection.Incoming
    )

    val longTextFrom = ChatMessage(
        "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but",
        contactId = "",
        messageDirection = MessageDirection.Incoming
    )

    val longTextTo = longTextFrom.copy(messageDirection = MessageDirection.Outgoing)
    val to =
        ChatMessage("Sample text to", contactId = "", messageDirection = MessageDirection.Outgoing)

    HangoutsTheme(true) {
        ChatView(
            listOf(from, longTextFrom, to, longTextTo, to, from, longTextTo, longTextFrom),
            Contact("1234", "Test"),
            {},
            true
        )
    }
}
