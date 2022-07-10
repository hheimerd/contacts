package com.hheimerd.hangouts.broadcast_receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import android.util.Log
import com.hheimerd.hangouts.data.models.ChatMessage
import com.hheimerd.hangouts.data.models.MessageDirection
import com.hheimerd.hangouts.data.repository.contacts.ContactRepository
import com.hheimerd.hangouts.data.repository.messages.MessageRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.StringBuilder
import javax.inject.Inject
import javax.inject.Singleton


@AndroidEntryPoint
class SmsBroadcastReceiver : BroadcastReceiver() {

    @Inject lateinit var contactRepository: ContactRepository;
    @Inject lateinit var messageRepository: MessageRepository;

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    companion object {
        const val SENT_INTENT = "com.hheimerd.hangouts.action.SMS_SENT"
        const val SENT_INTENT_ID_KEY = "com.hheimerd.hangouts.extra.EXTRA_SMS_SENT_ID"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Telephony.Sms.Intents.SMS_RECEIVED_ACTION -> receiveMessage(intent)
            SENT_INTENT -> handleMessageSent(intent)
        }
    }

    private fun receiveMessage(intent: Intent) {
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        val phone = messages[0].originatingAddress ?: return
        val messageBody = buildString {
            messages.map { sms ->
                append(sms.messageBody)
            }
        }

        coroutineScope.launch {
            val contact = contactRepository.getOrCreate(phone)

            val message = ChatMessage(
                text = messageBody,
                messageDirection = MessageDirection.Incoming,
                sent = true,
                contactId = contact.id,
                timestamp = messages[0].timestampMillis
            )

            messageRepository.addMessage(message)
        }
    }

    private fun handleMessageSent(intent: Intent) {
        val messageId = intent.getStringExtra(SENT_INTENT_ID_KEY)

        if (messageId != null) {
            coroutineScope.launch(Dispatchers.IO) {
                messageRepository.markSent(messageId);
            }
        }

    }

}