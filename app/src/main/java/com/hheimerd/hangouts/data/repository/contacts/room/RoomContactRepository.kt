package com.hheimerd.hangouts.data.repository.contacts.room

import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.data.repository.contacts.ContactRepository
import com.hheimerd.hangouts.data.repository.messages.room.MessageDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomContactRepository @Inject constructor(
    private val contactDao: ContactDao,
    private val messageDao: MessageDao
    ):
    ContactRepository {
    override fun getAll(): Flow<List<Contact>> {
        return contactDao.getAll();
    }

    override fun getById(id: String): Flow<Contact?> {
        return contactDao.getById(id);
    }

    override suspend fun insert(contact: Contact) {
        contactDao.insert(contact)
    }

    override suspend fun getOrCreate(phone: String): Contact {
        val normalizedPhone = Contact.normalizePhone(phone)
        var contact = contactDao.findByPhone(normalizedPhone)

        if (contact == null) {
            contact = Contact(normalizedPhone, normalizedPhone)
            insert(contact)
        }

        return contact
    }

    override suspend fun delete(contact: Contact) {
        contactDao.delete(contact)
        messageDao.deleteAllFrom(contact.id)
    }
}