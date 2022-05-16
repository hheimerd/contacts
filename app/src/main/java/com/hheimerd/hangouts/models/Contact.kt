package com.hheimerd.hangouts.models

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.hheimerd.hangouts.utils.random
import java.util.*

@Entity(tableName = Contact.TABLE_NAME)
data class Contact(
    val phone: String,
    val name: String,
    val secondName: String,

    val imageUri: String? = null,

    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
) {
    companion object {
        const val TABLE_NAME = "contacts"
    }
}