package com.hheimerd.hangouts.navigation

import com.hheimerd.hangouts.models.Contact

class Routes {
    companion object {
        const val Home = "home";
        const val Edit = "edit";
        const val Create = "create";
        const val Settings = "settings";

        fun Edit(contact: Contact): String {
            return "$Edit/${contact.id}"
        }

        fun Home(contact: Contact): String {
            return "$Home/?contactId=${contact.id}"
        }
    }
}