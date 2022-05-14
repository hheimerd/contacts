package com.hheimerd.hangouts.repository.database.room

import androidx.lifecycle.LiveData
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomContactRepository @Inject constructor(private val contactDao: ContactDao): ContactRepository {
    override fun getAll(limit: Int, offset: Int): Flow<List<Contact>> {
        return contactDao.getAll(limit, offset);
    }

    override fun getById(id: String): Flow<Contact> {
        return contactDao.getById(id);
    }

    override suspend fun create(contact: Contact) {
        contactDao.create(contact)
    }

    override suspend fun update(contact: Contact) {
        contactDao.update(contact)
    }

    override suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
    }
}