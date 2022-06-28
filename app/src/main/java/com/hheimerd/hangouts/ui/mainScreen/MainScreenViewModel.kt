package com.hheimerd.hangouts.ui.mainScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hheimerd.hangouts.events.UiEvent
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.navigation.Routes
import com.hheimerd.hangouts.data.repository.contacts.ContactRepository
import com.hheimerd.hangouts.viewModels.ViewModelWithUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val contactsRepository: ContactRepository,
    savedStateHandle: SavedStateHandle
) : ViewModelWithUiEvent() {

    var openedContact by mutableStateOf<Contact?>(null)
        private set

    init {
        val contactId = savedStateHandle.get<String>(Routes.contactIdParam)
        contactId?.let {
            viewModelScope.launch {
                getContactById(it).collect { contact ->
                    openedContact = contact
                }
            }
        }
    }

    fun onEvent(listEvent: MainViewEvent) {
        when (listEvent) {
            MainViewEvent.AddContactClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.AddEditContact))
            }
            is MainViewEvent.OpenContact -> sendUiEvent(UiEvent.Navigate(Routes.Home(listEvent.contact)))
            is MainViewEvent.EditContactClick -> sendUiEvent(UiEvent.Navigate(Routes.EditContact(listEvent.contact)))
            MainViewEvent.OpenSettings -> sendUiEvent(UiEvent.Navigate(Routes.Settings))
            MainViewEvent.UndoDelete -> TODO()
            is MainViewEvent.Delete -> {
                deleteContact(listEvent.contact) {
                    sendUiEvent(UiEvent.Navigate(Routes.Home))
                }
            }
        }
    }



    fun getAllContacts(limit: Int = 15, offset: Int = 0): Flow<List<Contact>> {
        return contactsRepository.getAll(limit, offset)
    }

    fun getContactById(id: String): Flow<Contact> {
        return contactsRepository.getById(id)
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