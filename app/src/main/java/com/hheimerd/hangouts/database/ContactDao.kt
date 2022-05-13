package com.hheimerd.hangouts.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import com.hheimerd.hangouts.models.Contact

interface ContactDao {
    @Insert
    fun create(contact: Contact)

    @Update
    fun update(contact: Contact)

    @Delete
    fun delete(contact: Contact)
}
