package com.hheimerd.hangouts.ui.contact_card

sealed class ContactCardEvent {
    object OpenChatClick: ContactCardEvent()
    object EditContactClick: ContactCardEvent()
    object DeleteContactClick: ContactCardEvent()
    object BackButtonClicked: ContactCardEvent()
    object Call: ContactCardEvent()
}