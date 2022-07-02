package com.hheimerd.hangouts.ui.add_edit_contact

sealed class AddEditContactEvent {

    object OnCloseButtonClick: AddEditContactEvent()
    object OnSettingsClick: AddEditContactEvent()

    data class OnSave(val contactState: AddEditContactStateHost): AddEditContactEvent()
}