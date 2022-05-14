package com.hheimerd.hangouts.repository.database.room

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.RoomMasterTable.TABLE_NAME
import com.hheimerd.hangouts.models.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {

    @Insert
    suspend fun create(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Delete
    suspend fun delete(contact: Contact)

    @Query("SELECT * FROM ${Contact.TABLE_NAME} WHERE id=:id")
    fun getById(id: String): Flow<Contact>

    @Query("SELECT * FROM ${Contact.TABLE_NAME} LIMIT :limit OFFSET :offset")
    fun getAll(offset: Int, limit: Int): Flow<List<Contact>>
}
