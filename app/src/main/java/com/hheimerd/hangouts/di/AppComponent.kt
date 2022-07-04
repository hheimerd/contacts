package com.hheimerd.hangouts.di

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.telephony.SmsManager
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.hheimerd.hangouts.data.repository.contacts.ContactRepository
import com.hheimerd.hangouts.data.repository.contacts.room.ContactDao
import com.hheimerd.hangouts.data.repository.contacts.room.ContactRoomDatabase
import com.hheimerd.hangouts.data.repository.contacts.room.RoomContactRepository
import com.hheimerd.hangouts.data.repository.messages.MessageRepository
import com.hheimerd.hangouts.data.repository.messages.room.MessageDao
import com.hheimerd.hangouts.data.repository.messages.room.MessageRoomDatabase
import com.hheimerd.hangouts.data.repository.messages.room.RoomMessageRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module(includes = [DatabaseBindingsModule::class])
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun getContactRoomDatabase(@ApplicationContext context: Context): ContactRoomDatabase {
        return Room
            .databaseBuilder(context, ContactRoomDatabase::class.java, ContactRoomDatabase.DATABASE_NAME)
            .build()
    }

    @Singleton
    @Provides
    fun getMessageRoomDatabase(@ApplicationContext context: Context): MessageRoomDatabase {
        return Room
            .databaseBuilder(context, MessageRoomDatabase::class.java, MessageRoomDatabase.DATABASE_NAME)
            .build()
    }

    @Provides
    fun getContactDao(contactRoomDatabase: ContactRoomDatabase): ContactDao {
        return contactRoomDatabase.getDao()
    }

    @Provides
    fun getMessageDao(messageRoomDatabase: MessageRoomDatabase): MessageDao {
        return messageRoomDatabase.getDao()
    }

    @Singleton
    @Provides
    fun getSmsManager(@ApplicationContext context: Context): SmsManager {
        return if (Build.VERSION.SDK_INT >= 23)
            context.getSystemService(SmsManager::class.java)
        else
            SmsManager.getDefault()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseBindingsModule {
    @Binds
    fun bindContactRepository(contactRepository: RoomContactRepository):
            ContactRepository

    @Binds
    fun bindMessageRepository(contactRepository: RoomMessageRepository):
            MessageRepository
}