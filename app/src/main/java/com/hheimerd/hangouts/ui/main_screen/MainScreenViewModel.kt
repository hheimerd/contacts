package com.hheimerd.hangouts.ui.main_screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hheimerd.hangouts.events.UiEvent
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.navigation.Routes
import com.hheimerd.hangouts.data.repository.contacts.ContactRepository
import com.hheimerd.hangouts.utils.extensions.runInIOThread
import com.hheimerd.hangouts.viewModels.ViewModelWithUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainScreenViewModel @Inject constructor(
    private val contactsRepository: ContactRepository
) : ViewModelWithUiEvent() {

    fun onEvent(listEvent: MainScreenEvent) {
        when (listEvent) {
            MainScreenEvent.AddContactClick -> {
                sendUiEvent(UiEvent.Navigate(Routes.AddEditContact))
            }
            is MainScreenEvent.OpenContact -> sendUiEvent(UiEvent.Navigate(Routes.ContactCard(listEvent.contact)))
            is MainScreenEvent.EditContactClick -> sendUiEvent(UiEvent.Navigate(Routes.EditContact(listEvent.contact)))
            MainScreenEvent.OpenSettings -> sendUiEvent(UiEvent.Navigate(Routes.Settings))
            MainScreenEvent.UndoDelete -> TODO()
            is MainScreenEvent.Delete -> {
                deleteContact(listEvent.contact) {
                    sendUiEvent(UiEvent.Navigate(Routes.Home))
                }
            }
        }
    }


    fun getAllContacts(limit: Int = 15, offset: Int = 0): Flow<List<Contact>> {
        return contactsRepository.getAll(limit, offset)
    }

    private fun deleteContact(contact: Contact, onSuccess: suspend () -> Unit = {}) {
        runInIOThread({
            contactsRepository.delete(contact)
        }, onSuccess)
    }


}