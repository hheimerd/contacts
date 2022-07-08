package com.hheimerd.hangouts.navigation

import com.hheimerd.hangouts.data.models.Contact

class Routes {
    companion object {
        const val contactIdParam = "contactId";
        const val Home = "home";
        const val AddEditContact = "addEdit/$contactIdParam={$contactIdParam}";
        const val Chat = "chat/$contactIdParam={$contactIdParam}";
        const val ContactCard = "contact/$contactIdParam={$contactIdParam}";
        const val Settings = "settings";

        fun EditContact(contact: Contact): String {
            return "addEdit/$contactIdParam=${contact.id}"
        }

        fun Chat(contact: Contact): String {
            return "chat/$contactIdParam=${contact.id}"
        }

        fun ContactCard(contact: Contact): String {
            return "contact/$contactIdParam=${contact.id}"
        }

    }
}