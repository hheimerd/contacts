package com.hheimerd.hangouts.screens

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.components.EditContact
import com.hheimerd.hangouts.events.ContactEvent
import com.hheimerd.hangouts.events.UiEvent
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.navigation.Routes
import com.hheimerd.hangouts.utils.typeUtils.ActionWith
import com.hheimerd.hangouts.viewModels.ContactsViewModel

@Composable
fun EditContactScreen(
    contactsViewModel: ContactsViewModel,
    contactId: String,
    onUiEvent: ActionWith<UiEvent>,
    modifier: Modifier = Modifier
) {
    val contact = contactsViewModel.getContactById(contactId).collectAsState(initial = Contact("",""))

    EditContact(
        onSave = { updated ->
            contactsViewModel.onEvent(ContactEvent.Insert(updated))
        },
        initialValue = contact.value,
        title = stringResource(id = R.string.create_contact_title),
        onOpenSettingsClick = {onUiEvent(UiEvent.Navigate(Routes.Settings))},
        onClose = {onUiEvent(UiEvent.PopBack)},
        modifier = modifier
    )
}
