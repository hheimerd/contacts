package com.hheimerd.hangouts.ui.add_edit_contact

import android.telephony.PhoneNumberUtils
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
import com.hheimerd.hangouts.utils.extensions.runInIOThread
import com.hheimerd.hangouts.viewModels.ViewModelWithUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditContactViewModel @Inject constructor(
    private val contactsRepository: ContactRepository,
    savedStateHandle: SavedStateHandle
) : ViewModelWithUiEvent() {

    var initialContact by mutableStateOf<Contact?>(null);
    var state by mutableStateOf<AddEditContactStateHost?>(null);

    init {
        val contactId = savedStateHandle.get<String>(Routes.contactIdParam)!!

        viewModelScope.launch {
            contactsRepository.getById(contactId).collect { foundedContact ->
                initialContact = foundedContact ?: Contact("", "");
                state = AddEditContactStateHost(foundedContact)
            };
        }
    }

    fun onEvent(event: AddEditContactEvent) {
        when (event) {
            AddEditContactEvent.OnCloseButtonClick -> sendUiEvent(UiEvent.PopBack,)
            AddEditContactEvent.OnSettingsClick -> sendUiEvent(UiEvent.Navigate(Routes.Settings),)
            is AddEditContactEvent.OnSave -> {
                event.contactState.let { contactState ->
                    // Contact Validation

                    contactState.resetErrors()
                    if (contactState.phone.value.isBlank()) {
                        contactState.phoneError.value = StringResource(R.string.phone_cant_be_empty)
                    } else if (PhoneNumberUtils.isGlobalPhoneNumber(contactState.phone.value) == false) {
                        contactState.phoneError.value =
                            StringResource(R.string.incorrect_phone_format)
                    }
                    if (contactState.firstName.value.isBlank()) {
                        contactState.firstNameError.value =
                            StringResource(R.string.name_cant_be_empty)
                    }

                    if (contactState.allFieldsValid()) {
                        initialContact?.let { initial ->
                            val newContact = initial.copy(
                                firstName = contactState.firstName.value,
                                lastName = contactState.lastName.value,
                                phone = contactState.phone.value,
                                email = contactState.email.value,
                                nickname = contactState.nickname.value,
                                imageUri = contactState.imageUri.value,
                            )
                            runInIOThread({
                                contactsRepository.insert(
                                    newContact
                                )
                            }) {
                                sendUiEvent(UiEvent.Navigate(Routes.ContactCard(newContact)),)
                            }
                        }
                    }
                }
            }
        }
    }

}
