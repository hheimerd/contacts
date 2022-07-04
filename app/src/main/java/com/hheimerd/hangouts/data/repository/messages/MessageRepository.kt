package com.hheimerd.hangouts.data.repository.messages

import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.data.models.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun addMessage(message: Message)

    fun getMessagesFrom(contact: Contact): Flow<List<Message>>

    suspend fun deleteMessagesFrom(contact: Contact)

    suspend fun markSent(messageId: String)
}