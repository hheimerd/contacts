package com.hheimerd.hangouts.data.repository.messages.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hheimerd.hangouts.data.models.ChatMessage

@Database(entities = [ChatMessage::class], version = 1, exportSchema = false)
abstract class MessageRoomDatabase : RoomDatabase() {
    abstract fun getDao(): MessageDao

    companion object {
        const val DATABASE_NAME = "messages"
    }
}