package com.hheimerd.hangouts.data.repository.messages.room

import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.data.models.ChatMessage
import com.hheimerd.hangouts.data.repository.messages.MessageRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomMessageRepository @Inject constructor(private val messageDao: MessageDao) : MessageRepository{
    override suspend fun addMessage(chatMessage: ChatMessage) {
        messageDao.add(chatMessage)
    }

    override fun getMessagesFrom(contact: Contact): Flow<List<ChatMessage>> {
        return messageDao.getAllFrom(contactId = contact.id)
    }

    override suspend fun deleteMessagesFrom(contact: Contact) {
        messageDao.deleteAllFrom(contactId = contact.id)
    }

    override suspend fun markSent(messageId: String) {
        messageDao.markSent(messageId)
    }

}