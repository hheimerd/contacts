package com.hheimerd.hangouts.events

import com.hheimerd.hangouts.models.Contact

sealed class ContactEvent {
    data class Delete(val contact: Contact): ContactEvent();
    data class Insert(val contact: Contact): ContactEvent();
    object UndoDelete: ContactEvent();
    data class Open(val contact: Contact): ContactEvent();
    object Add: ContactEvent()
    data class Edit(val contact: Contact): ContactEvent();
}