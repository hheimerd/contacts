package com.hheimerd.hangouts.di

import android.content.Context
import androidx.room.Room
import com.hheimerd.hangouts.repository.contacts.ContactRepository
import com.hheimerd.hangouts.repository.contacts.room.ContactDao
import com.hheimerd.hangouts.repository.contacts.room.ContactRoomDatabase
import com.hheimerd.hangouts.repository.contacts.room.RoomContactRepository
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
    fun getContactsRoomDatabase(@ApplicationContext context: Context): ContactRoomDatabase {
        return Room
            .databaseBuilder(context, ContactRoomDatabase::class.java, ContactRoomDatabase.DATABASE_NAME)
            .build()
    }

    @Provides
    fun getContactsDao(contactRoomDatabase: ContactRoomDatabase): ContactDao {
        return contactRoomDatabase.getDao()
    }
}

@Module
@InstallIn(SingletonComponent::class)
interface DatabaseBindingsModule {
    @Binds
    fun bindContactsRepository(contactRepository: RoomContactRepository):
            ContactRepository
}