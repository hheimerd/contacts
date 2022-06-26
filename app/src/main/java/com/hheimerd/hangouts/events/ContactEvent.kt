package com.hheimerd.hangouts.events

import com.hheimerd.hangouts.models.Contact

sealed class ContactEvent {
    object Create: ContactEvent()
    data class Update(val contact: Contact): ContactEvent();
    data class Delete(val contact: Contact): ContactEvent();
    data class Open(val contact: Contact): ContactEvent();
}