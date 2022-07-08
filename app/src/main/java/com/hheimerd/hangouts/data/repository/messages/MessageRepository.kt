package com.hheimerd.hangouts.data.repository.messages

import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.data.models.ChatMessage
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun addMessage(chatMessage: ChatMessage)

    fun getMessagesFrom(contact: Contact): Flow<List<ChatMessage>>

    suspend fun deleteMessagesFrom(contact: Contact)

    suspend fun markSent(messageId: String)
}