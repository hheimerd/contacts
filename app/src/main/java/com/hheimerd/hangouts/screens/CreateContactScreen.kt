package com.hheimerd.hangouts.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.components.EditContact
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.utils.typeUtils.Action
import com.hheimerd.hangouts.utils.typeUtils.ActionWith
import com.hheimerd.hangouts.viewModels.ContactsViewModel

@Composable
fun CreateContactScreen(
    contactsViewModel: ContactsViewModel,
    onOpenSettingsClick: Action,
    onCreated: ActionWith<Contact>,
    onClose: Action,
    modifier: Modifier = Modifier
) {
    EditContact(
        onSave = { contact ->
            contactsViewModel.createContact(
                contact,
                onSuccess = { onCreated(contact) })
        },
        initialValue = Contact("", ""),
        title = stringResource(id = R.string.create_contact_title),
        onOpenSettingsClick = onOpenSettingsClick,
        onClose = onClose,
        modifier = modifier
    )
}
