package com.hheimerd.hangouts.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.hheimerd.hangouts.MainActivity

abstract class ContactDatabase : RoomDatabase() {

    abstract fun getDao(): ContactDao

    companion object {
        private const val DATABASE_NAME = "contacts"

        @Volatile
        var INSTANCE: ContactDatabase? = null;

        fun getInstance(): ContactDatabase {
            if (INSTANCE == null)
                throw UninitializedPropertyAccessException("INSTANCE not initialized")

            return INSTANCE!!;
        }

        fun init(context: Context) {
            if (INSTANCE != null) return;

            INSTANCE = Room
                .databaseBuilder(context, ContactDatabase::class.java, DATABASE_NAME)
                .build()
        }
    }


}