package com.hheimerd.hangouts.broadcast_receivers

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Telephony
import android.util.Log
import androidx.annotation.RequiresApi
import com.hheimerd.hangouts.data.models.Message
import com.hheimerd.hangouts.data.models.MessageDirection
import com.hheimerd.hangouts.data.repository.contacts.ContactRepository
import com.hheimerd.hangouts.data.repository.messages.MessageRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SmsBroadcastReceiver @Inject constructor(
    private val contactRepository: ContactRepository,
    private val messageRepository: MessageRepository,
) : BroadcastReceiver() {

    val coroutineScope = CoroutineScope(Dispatchers.IO)

    companion object {
        const val SENT_INTENT = "SMS_SENT"
        const val SENT_INTENT_ID_KEY = "SMS_SENT_ID"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Telephony.Sms.Intents.SMS_RECEIVED_ACTION -> receiveMessage(intent)
            SENT_INTENT -> handleMessageSent(intent)
        }


    }

    private fun receiveMessage(intent: Intent) {
        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent)
        messages.forEach { sms ->
            val phone = sms.originatingAddress
            Log.d("Receive phone", phone.toString())

            if (phone == null)
                return;

            coroutineScope.launch {
                val contact = contactRepository.getOrCreate(phone)

                val message = Message(
                    message = sms.messageBody,
                    messageDirection = MessageDirection.Incoming,
                    sent = true,
                    contactId = contact.id
                )

                messageRepository.addMessage(message)
            }

        }
    }

    private fun handleMessageSent(intent: Intent) {
        val messageId = intent.getStringExtra(SENT_INTENT_ID_KEY)

        if (messageId != null) {
            coroutineScope.launch {
                messageRepository.markSent(messageId);
            }
        }

    }

}