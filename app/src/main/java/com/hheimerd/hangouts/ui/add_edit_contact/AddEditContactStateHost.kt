package com.hheimerd.hangouts.ui.add_edit_contact

import androidx.compose.runtime.mutableStateOf
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.ui.StringResource

class AddEditContactStateHost(contact: Contact? = null) {
    var firstName = mutableStateOf(contact?.firstName ?: "")
    var firstNameError = mutableStateOf<StringResource?>(null)

    var lastName = mutableStateOf(contact?.lastName ?: "")
    var lastNameError = mutableStateOf<StringResource?>(null)

    var phone = mutableStateOf(contact?.phone ?: "")
    var phoneError = mutableStateOf<StringResource?>(null)

    var email = mutableStateOf(contact?.email ?: "")
    var emailError = mutableStateOf<StringResource?>(null)

    var nickname = mutableStateOf(contact?.nickname ?: "")
    var nicknameError = mutableStateOf<StringResource?>(null)

    var imageUri = mutableStateOf(contact?.imageUri ?: "")

    val hasInitialContact = contact != null

    fun allFieldsValid(): Boolean {
        return firstNameError.value == null &&
                lastNameError.value == null &&
                phoneError.value == null &&
                emailError.value == null &&
                nicknameError.value == null
    }

    fun resetErrors() {
        firstNameError.value = null
        lastNameError.value = null
        phoneError.value = null
        emailError.value = null
        nicknameError.value = null
    }
}