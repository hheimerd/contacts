package com.hheimerd.hangouts.data.repository.messages.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.hheimerd.hangouts.data.models.ChatMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM ${ChatMessage.TableName} WHERE contactId = :contactId ORDER BY timestamp")
    fun getAllFrom(contactId: String): Flow<List<ChatMessage>>

    @Insert()
    fun add(chatMessage: ChatMessage)

    @Query("DELETE FROM ${ChatMessage.TableName} WHERE contactId = :contactId")
    fun deleteAllFrom(contactId: String)

    @Query("UPDATE ${ChatMessage.TableName} SET sent = 1 WHERE id = :messageId")
    fun markSent(messageId: String)

}