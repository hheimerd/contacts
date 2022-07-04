package com.hheimerd.hangouts.data.repository.contacts.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.RoomMasterTable.TABLE_NAME
import com.hheimerd.hangouts.data.models.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("SELECT * FROM ${Contact.TABLE_NAME} WHERE id=:id")
    fun getById(id: String): Flow<Contact>

    @Query("SELECT * FROM ${Contact.TABLE_NAME}")
    fun getAll(): Flow<List<Contact>>

    @Query("SELECT * FROM ${Contact.TABLE_NAME} WHERE phone = :phone")
    suspend fun findByPhone(phone: String): Contact?
}
