package com.hheimerd.hangouts.navigation

import com.hheimerd.hangouts.data.models.Contact

class Routes {
    companion object {
        const val contactIdParam = "contactId";
        const val Home = "home";
        const val AddEditContact = "addEdit/$contactIdParam={$contactIdParam}";
        const val Settings = "settings";

        fun EditContact(contact: Contact): String {
            return "addEdit/$contactIdParam=${contact.id}"
        }

        fun Home(contact: Contact): String {
            return "$Home/?contactId=${contact.id}"
        }

    }
}