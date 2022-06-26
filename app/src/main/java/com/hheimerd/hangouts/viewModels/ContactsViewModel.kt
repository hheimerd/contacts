package com.hheimerd.hangouts.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hheimerd.hangouts.events.ContactEvent
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.repository.contacts.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(private val contactsRepository: ContactRepository) :
    ViewModel() {

    fun onEvent(event: ContactEvent) {
        when (event) {
            ContactEvent.Create -> TODO()
            is ContactEvent.Delete -> TODO()
            is ContactEvent.Open -> TODO()
            is ContactEvent.Update -> TODO()
        }
    }

    fun getAllContacts(limit: Int = 15, offset: Int = 0): Flow<List<Contact>> {
        return contactsRepository.getAll(limit, offset)
    }

    fun getContactById(id: String): Flow<Contact> {
        return contactsRepository.getById(id)
    }

    fun createContact(contact: Contact, onSuccess: suspend () -> Unit = {}) {
        runInIOThread({
            contactsRepository.create(contact)
        }, onSuccess)
    }

    fun updateContact(contact: Contact, onSuccess: suspend () -> Unit = {}) {
        runInIOThread({
            contactsRepository.update(contact)
        }, onSuccess)
    }

    fun deleteContact(contact: Contact, onSuccess: suspend () -> Unit = {}) {
        runInIOThread({
            contactsRepository.delete(contact)
        }, onSuccess)
    }

    private fun runInIOThread(
        block: suspend () -> Unit,
        onSuccess: suspend () -> Unit
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            block()
            viewModelScope.launch(Dispatchers.Main) {
                onSuccess()
            }
        }
    }

}