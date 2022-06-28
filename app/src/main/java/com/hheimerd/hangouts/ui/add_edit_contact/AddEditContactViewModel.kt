package com.hheimerd.hangouts.ui.add_edit_contact

import androidx.compose.material.SnackbarDuration
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.navigation.Routes
import com.hheimerd.hangouts.data.repository.contacts.ContactRepository
import com.hheimerd.hangouts.events.UiEvent
import com.hheimerd.hangouts.ui.StringResource
import com.hheimerd.hangouts.viewModels.ViewModelWithUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditContactViewModel @Inject constructor(
    private val contactsRepository: ContactRepository,
    savedStateHandle: SavedStateHandle
) : ViewModelWithUiEvent() {

    var initialContact by mutableStateOf<Contact?>(null);

    init {
        val contactId = savedStateHandle.get<String>(Routes.contactIdParam);
        if (contactId == null)
            initialContact = Contact("", "");
        else
            viewModelScope.launch {
                contactsRepository.getById(contactId).collect {
                    initialContact = it;
                };
            }
    }

    fun onEvent(event: AddEditContactEvent) {
        when (event) {
            AddEditContactEvent.OnCloseButtonClick -> sendUiEvent(UiEvent.PopBack)
            AddEditContactEvent.OnSettingsClick -> sendUiEvent(UiEvent.Navigate(Routes.Settings))
            is AddEditContactEvent.OnSave -> {
                event.contact.let { newContact ->
                    if (newContact.phone.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                StringResource(R.string.phone_cant_be_empty),
                                duration = SnackbarDuration.Short
                            )
                        )
                    }
                    if (newContact.firstName.isBlank()) {
                        sendUiEvent(
                            UiEvent.ShowSnackbar(
                                StringResource(R.string.name_cant_be_empty),
                                duration = SnackbarDuration.Short
                            )
                        )
                    }

                    if (newContact.firstName.isNotBlank() && newContact.phone.isNotBlank()) {
                        saveContact(newContact)
                    }
                }
            }
        }
    }

    fun saveContact(contact: Contact) {
        viewModelScope.launch(Dispatchers.IO) {
            contactsRepository.insert(
                contact
            )
            sendUiEvent(UiEvent.Navigate(Routes.Home))
        }
    }
}
