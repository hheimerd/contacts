package com.hheimerd.hangouts.data.repository.messages.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hheimerd.hangouts.data.models.Message
import com.hheimerd.hangouts.data.repository.contacts.room.ContactDao

@Database(entities = [Message::class], version = 1, exportSchema = false)
abstract class MessageRoomDatabase : RoomDatabase() {
    abstract fun getDao(): MessageDao

    companion object {
        const val DATABASE_NAME = "messages"
    }
}