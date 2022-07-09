package com.hheimerd.hangouts.data.models

import android.telephony.PhoneNumberUtils
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.hheimerd.hangouts.utils.random
import java.util.*

@Entity(tableName = Contact.TABLE_NAME)
data class Contact(
    val phone: String,
    val firstName: String,
    val lastName: String = "",
    val email: String = "",
    val nickname: String = "",

    val imageUri: String? = null,

    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
) {
    companion object {
        const val TABLE_NAME = "contacts"

        fun normalizePhone(phoneString: String): String {
            var normalized = PhoneNumberUtils.normalizeNumber(phoneString)
            if (normalized.length == 11 && normalized.first() == '8')
                normalized = "+7" + normalized.substring(1)
            return normalized
        }
    }

    val fullName: String
        @Ignore
        get() {
            return "$firstName $lastName"
        }
}
