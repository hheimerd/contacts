package com.hheimerd.hangouts.ui.contact_card

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.data.repository.contacts.ContactRepository
import com.hheimerd.hangouts.events.UiEvent
import com.hheimerd.hangouts.navigation.Routes
import com.hheimerd.hangouts.utils.InternalStorage
import com.hheimerd.hangouts.utils.extensions.runInIOThread
import com.hheimerd.hangouts.viewModels.ViewModelWithUiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ContactCardViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val internalStorage: InternalStorage,
    savedStateHandle: SavedStateHandle
) : ViewModelWithUiEvent() {
    var contact by mutableStateOf<Contact?>(null);

    init {
        val contactId = savedStateHandle.get<String>(Routes.contactIdParam)
        contactId?.let {
            viewModelScope.launch {
                contactRepository.getById(contactId).collect { foundContact ->
                    contact = foundContact
                }
            }
        }
    }


    fun onEvent(event: ContactCardEvent) {
        when (event) {
            ContactCardEvent.BackButtonClicked -> sendUiEvent(
                UiEvent.Navigate(Routes.Home, true)
            )
            ContactCardEvent.DeleteContactClick -> {
                contact?.let {
                    runInIOThread({
                        contactRepository.delete(it)
                        if (it.imageUri.isNullOrBlank() == false) {
                            internalStorage.deletePhoto(it.imageUri)
                        }
                    }) {
                        sendUiEvent(
                            UiEvent.Navigate(Routes.Home, true)
                        )
                    }
                }
            }
            ContactCardEvent.EditContactClick -> {
                contact?.let {
                    sendUiEvent(
                        UiEvent.Navigate(Routes.EditContact(it))
                    )
                }
            }
            ContactCardEvent.OpenChatClick -> contact?.let {
                sendUiEvent(UiEvent.Navigate(Routes.Chat(it)))
            }
            ContactCardEvent.Call -> {
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact?.phone))
                sendUiEvent(UiEvent.StartActivity(intent, Manifest.permission.CALL_PHONE))
            }
        }
    }

}