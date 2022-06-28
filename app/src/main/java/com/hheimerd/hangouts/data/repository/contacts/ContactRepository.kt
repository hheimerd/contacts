package com.hheimerd.hangouts.data.repository.contacts

import com.hheimerd.hangouts.data.models.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getAll(limit: Int, offset: Int): Flow<List<Contact>>

    fun getById(id: String): Flow<Contact>

    suspend fun insert(contact: Contact)

    suspend fun delete(contact: Contact)
}