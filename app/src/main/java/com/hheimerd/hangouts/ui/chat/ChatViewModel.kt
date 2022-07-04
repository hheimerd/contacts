package com.hheimerd.hangouts.ui.chat

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.telephony.SmsManager
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.broadcast_receivers.SmsBroadcastReceiver
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.data.models.Message
import com.hheimerd.hangouts.data.models.MessageDirection
import com.hheimerd.hangouts.data.repository.contacts.ContactRepository
import com.hheimerd.hangouts.data.repository.messages.MessageRepository
import com.hheimerd.hangouts.events.UiEvent
import com.hheimerd.hangouts.navigation.Routes
import com.hheimerd.hangouts.ui.StringResource
import com.hheimerd.hangouts.viewModels.ViewModelWithUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    val messageRepository: MessageRepository,
    private val smsManager: SmsManager,
    contactRepository: ContactRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModelWithUiEvent() {
    var contact by mutableStateOf<Contact?>(null)
        private set;

    var sendSmsAllowed = false
        private set

    var messages by mutableStateOf<List<Message>>(listOf())
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


    private fun onSmsPermissionResult(allowed: Boolean) {
        sendSmsAllowed = allowed
        if (allowed) return

        sendUiEvent(
            UiEvent.ShowSnackbar(
                StringResource(R.string.sms_permission_deny_snack),
                StringResource(R.string.retry),
            ) {
                requestSmsPermission()
            },
        )
    }

    fun requestSmsPermission() {
        sendUiEvent(
            UiEvent.RequestPermission(
                Manifest.permission.SEND_SMS,
                ::onSmsPermissionResult
            )
        )
    }

    fun sendMessage(messageText: String) {
        if (contact == null || sendSmsAllowed == false)
            return

        val message = Message(
            contactId = contact!!.id,
            message = messageText,
            messageDirection = MessageDirection.Outgoing,
        )

        val intent = Intent(SmsBroadcastReceiver.SENT_INTENT);
        intent.putExtra(SmsBroadcastReceiver.SENT_INTENT_ID_KEY, message.id)

        val sentPendingIntent = PendingIntent.getBroadcast(null, 0, intent, 0)

        smsManager.sendTextMessage(
            contact!!.phone,
            null,
            messageText,
            sentPendingIntent,
            null
        )
    }
}