package com.hheimerd.hangouts.repository.database.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hheimerd.hangouts.models.Contact

@Database(entities = [Contact::class], version = 1)
abstract class ContactRoomDatabase : RoomDatabase() {

    abstract fun getDao(): ContactDao

    companion object {
        const val DATABASE_NAME = "contacts"
    }


}