package com.hheimerd.hangouts.repository

import com.hheimerd.hangouts.models.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getAll(limit: Int, offset: Int): Flow<List<Contact>>

    fun getById(string: String): Flow<Contact>

    suspend fun create(contact: Contact)

    suspend fun update(contact: Contact)

    suspend fun delete(contact: Contact)
}