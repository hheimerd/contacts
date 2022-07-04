package com.hheimerd.hangouts.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Timestamp
import java.util.UUID

enum class MessageDirection {
    Outgoing,
    Incoming
}

@Entity(tableName = Message.TableName)
data class Message(
    val message: String,
    val contactId: String,
    val sent: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()/1000,
    val messageDirection: MessageDirection,

    @PrimaryKey
    val id: String = UUID.randomUUID().toString()
) {
    companion object {
        const val TableName = "messages"
    }
}