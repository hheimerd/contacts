package com.hheimerd.hangouts.ui.add_edit_contact

import com.hheimerd.hangouts.data.models.Contact

sealed class AddEditContactEvent {

    object OnCloseButtonClick: AddEditContactEvent()
    object OnSettingsClick: AddEditContactEvent()

    data class OnSave(val contact: Contact): AddEditContactEvent()
}