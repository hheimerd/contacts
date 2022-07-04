package com.hheimerd.hangouts.data.repository.contacts

import android.content.Context
import com.hheimerd.hangouts.data.models.Contact
import kotlinx.coroutines.flow.Flow

interface ContactRepository {

    fun getAll(): Flow<List<Contact>>

    fun getById(id: String): Flow<Contact?>

    suspend fun insert(contact: Contact)

    suspend fun getOrCreate(phone: String): Contact

    suspend fun delete(contact: Contact)
}