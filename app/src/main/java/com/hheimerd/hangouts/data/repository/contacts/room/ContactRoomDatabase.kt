package com.hheimerd.hangouts.data.repository.contacts.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hheimerd.hangouts.data.models.Contact

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class ContactRoomDatabase : RoomDatabase() {

    abstract fun getDao(): ContactDao

    companion object {
        const val DATABASE_NAME = "contacts"
    }


}