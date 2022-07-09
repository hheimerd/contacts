package com.hheimerd.hangouts.ui.chat

import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telephony.SmsManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hheimerd.hangouts.broadcast_receivers.SmsBroadcastReceiver
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.data.models.ChatMessage
import com.hheimerd.hangouts.data.models.MessageDirection
import com.hheimerd.hangouts.data.repository.contacts.ContactRepository
import com.hheimerd.hangouts.data.repository.messages.MessageRepository
import com.hheimerd.hangouts.events.UiEvent
import com.hheimerd.hangouts.navigation.Routes
import com.hheimerd.hangouts.utils.extensions.runInIOThread
import com.hheimerd.hangouts.viewModels.ViewModelWithUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val messageRepository: MessageRepository,
    private val smsManager: SmsManager?,
    contactRepository: ContactRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModelWithUiEvent() {
    var sendMessageAllowed by mutableStateOf(false)
        private set

    var contact by mutableStateOf<Contact?>(null)
        private set;

    var messages by mutableStateOf<List<ChatMessage>>(listOf())
        private set

    init {
        val contactId = savedStateHandle.get<String>(Routes.contactIdParam)!!

        viewModelScope.launch {
            contactRepository.getById(contactId).collect { foundContact ->
                contact = foundContact
                if (foundContact != null) {
                    messageRepository.getMessagesFrom(foundContact).collect { actualMessages ->
                        messages = actualMessages
                    }
                }
            }
        }
    }

    fun onEvent(chatEvent: ChatEvent) {
        when (chatEvent) {
            ChatEvent.GoBack -> sendUiEvent(UiEvent.PopBack)
            ChatEvent.RemoveChat -> {
                runInIOThread({
                    messageRepository.deleteMessagesFrom(contact!!)
                }) {
                    sendUiEvent(UiEvent.Navigate(Routes.Home, true))
                }
            }
            is ChatEvent.SendMessage -> {
                sendMessage(chatEvent.message, chatEvent.context)
            }
            is ChatEvent.SendSmsPermissionResult ->
                sendMessageAllowed = chatEvent.permissionAllowed && contact != null && smsManager != null
        }
    }

    private fun sendMessage(messageText: String, context: Context) {
        if (!sendMessageAllowed)
            return

        val message = ChatMessage(
            contactId = contact!!.id,
            text = messageText,
            messageDirection = MessageDirection.Outgoing,
        )

        val intent = Intent(context, SmsBroadcastReceiver::class.java);
        intent.putExtra(SmsBroadcastReceiver.SENT_INTENT_ID_KEY, message.id)
        intent.action = SmsBroadcastReceiver.SENT_INTENT

        val sentPendingIntent = PendingIntent.getBroadcast(
            context,
            0,
            intent,
            if (Build.VERSION.SDK_INT > 23) FLAG_IMMUTABLE else 0
        )

        runInIOThread({ messageRepository.addMessage(message) }) {
            smsManager?.sendTextMessage(
                contact!!.phone,
                null,
                messageText,
                sentPendingIntent,
                null
            )
        }
    }
}