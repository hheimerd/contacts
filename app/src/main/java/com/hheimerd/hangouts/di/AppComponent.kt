package com.hheimerd.hangouts.di

import android.content.Context
import android.os.Build
import android.telephony.SmsManager
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

@Module(includes = [MessageBindingsModule::class])
@InstallIn(SingletonComponent::class)
class MessagesModule {
    @Singleton
    @Provides
    fun getMessageRoomDatabase(@ApplicationContext context: Context): MessageRoomDatabase {
        return Room
            .databaseBuilder(
                context,
                MessageRoomDatabase::class.java,
                MessageRoomDatabase.DATABASE_NAME
            )
            .build()
    }

    @Provides
    fun getMessageDao(messageRoomDatabase: MessageRoomDatabase): MessageDao {
        return messageRoomDatabase.getDao()
    }

    @Singleton
    @Provides
    fun getSmsManager(@ApplicationContext context: Context): SmsManager? {
        return if (Build.VERSION.SDK_INT >= 23)
            context.getSystemService(SmsManager::class.java)
        else
            SmsManager.getDefault()
    }


}


@Module
@InstallIn(SingletonComponent::class)
interface MessageBindingsModule {
    @Binds
    fun bindMessageRepository(contactRepository: RoomMessageRepository):
            MessageRepository

}

@Module(includes = [ContactBindingsModule::class])
@InstallIn(SingletonComponent::class)
class ContactModule {
    @Singleton
    @Provides
    fun getContactRoomDatabase(@ApplicationContext context: Context): ContactRoomDatabase {
        return Room
            .databaseBuilder(
                context,
                ContactRoomDatabase::class.java,
                ContactRoomDatabase.DATABASE_NAME
            )
            .build()
    }


    @Provides
    fun getContactDao(contactRoomDatabase: ContactRoomDatabase): ContactDao {
        return contactRoomDatabase.getDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface ContactBindingsModule {
    @Binds
    fun bindContactRepository(contactRepository: RoomContactRepository):
            ContactRepository

}