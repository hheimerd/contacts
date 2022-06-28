package com.hheimerd.hangouts.ui.mainScreen

import com.hheimerd.hangouts.data.models.Contact

sealed class MainViewEvent {
    data class Delete(val contact: Contact): MainViewEvent();
    object UndoDelete: MainViewEvent();
    data class OpenContact(val contact: Contact): MainViewEvent();
    data class EditContactClick(val contact: Contact): MainViewEvent();
    object AddContactClick: MainViewEvent()
    object OpenSettings: MainViewEvent()
}