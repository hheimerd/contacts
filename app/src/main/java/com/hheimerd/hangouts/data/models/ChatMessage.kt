package com.hheimerd.hangouts.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class MessageDirection {
    Outgoing,
    Incoming
}

@Entity(tableName = ChatMessage.TableName)
data class ChatMessage(
    val text: String,
    val contactId: String,
    val sent: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()/1000,
    val messageDirection: MessageDirection = MessageDirection.Outgoing,

    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
) {
    companion object {
        const val TableName = "messages"
    }
}