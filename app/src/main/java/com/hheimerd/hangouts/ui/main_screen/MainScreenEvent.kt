package com.hheimerd.hangouts.ui.main_screen

import com.hheimerd.hangouts.data.models.Contact

sealed class MainScreenEvent {
    data class Delete(val contact: Contact): MainScreenEvent();
    object UndoDelete: MainScreenEvent();
    data class OpenContact(val contact: Contact): MainScreenEvent();
    data class EditContactClick(val contact: Contact): MainScreenEvent();
    object AddContactClick: MainScreenEvent()
    object OpenSettings: MainScreenEvent()
}