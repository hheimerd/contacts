package com.hheimerd.hangouts.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.events.ContactEvent
import com.hheimerd.hangouts.events.UiEvent
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.navigation.Routes
import com.hheimerd.hangouts.repository.contacts.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(private val contactsRepository: ContactRepository) :
    ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: ContactEvent) {
        when (event) {
            ContactEvent.Add -> {
                sendUiEvent(UiEvent.Navigate(Routes.Create))
            }
            is ContactEvent.Insert -> {
                insertContact(event.contact) {
                    sendUiEvent(UiEvent.Navigate(Routes.Home))
                }
            }
            is ContactEvent.Open -> sendUiEvent(UiEvent.Navigate(Routes.Home(event.contact)))
            is ContactEvent.Edit -> sendUiEvent(UiEvent.Navigate(Routes.Edit(event.contact)))
            ContactEvent.UndoDelete -> TODO()
            is ContactEvent.Delete -> {
                deleteContact(event.contact) {
                    sendUiEvent(UiEvent.Navigate(Routes.Home))
                }
            }
        }
    }
    private fun sendUiEvent(event: UiEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }

    fun getAllContacts(limit: Int = 15, offset: Int = 0): Flow<List<Contact>> {
        return contactsRepository.getAll(limit, offset)
    }

    fun getContactById(id: String): Flow<Contact> {
        return contactsRepository.getById(id)
    }

    private fun insertContact(contact: Contact, onSuccess: suspend () -> Unit = {}) {
        runInIOThread({
            contactsRepository.insert(contact)
        }, onSuccess)
    }

    private fun deleteContact(contact: Contact, onSuccess: suspend () -> Unit = {}) {
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