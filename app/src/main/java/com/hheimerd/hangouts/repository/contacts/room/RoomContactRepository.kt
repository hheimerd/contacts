package com.hheimerd.hangouts.repository.contacts.room

import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.repository.contacts.ContactRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomContactRepository @Inject constructor(private val contactDao: ContactDao):
    ContactRepository {
    override fun getAll(limit: Int, offset: Int): Flow<List<Contact>> {
        return contactDao.getAll(limit, offset);
    }

    override fun getById(id: String): Flow<Contact> {
        return contactDao.getById(id);
    }

    override suspend fun insert(contact: Contact) {
        contactDao.insert(contact)
    }

    override suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
    }
}