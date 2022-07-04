package com.hheimerd.hangouts.data.repository.messages.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.hheimerd.hangouts.data.models.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Query("SELECT * FROM ${Message.TableName} WHERE contactId = :contactId")
    fun getAllFrom(contactId: String): Flow<List<Message>>

    @Insert()
    fun add(message: Message)

    @Query("DELETE FROM ${Message.TableName} WHERE contactId = :contactId")
    fun deleteAllFrom(contactId: String)

    @Query("UPDATE ${Message.TableName} SET sent = 1 WHERE id = :messageId")
    fun markSent(messageId: String)

}