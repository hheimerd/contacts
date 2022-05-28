package com.hheimerd.hangouts.screens

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.components.EditContact
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.utils.typeUtils.Action
import com.hheimerd.hangouts.utils.typeUtils.ActionWith
import com.hheimerd.hangouts.viewModels.ContactsViewModel

@Composable
fun EditContactScreen(
    contactsViewModel: ContactsViewModel,
    contactId: String,
    onOpenSettingsClick: Action,
    onUpdated: ActionWith<Contact>,
    onClose: Action,
    modifier: Modifier = Modifier
) {
    val contact = contactsViewModel.getContactById(contactId).collectAsState(initial = Contact("",""))

    EditContact(
        onSave = { updated ->
            contactsViewModel.updateContact(
                updated,
                onSuccess = { onUpdated(updated) })
        },
        initialValue = contact.value,
        title = stringResource(id = R.string.create_contact_title),
        onOpenSettingsClick = onOpenSettingsClick,
        onClose = onClose,
        modifier = modifier
    )
}
