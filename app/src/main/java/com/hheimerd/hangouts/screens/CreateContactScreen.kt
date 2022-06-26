package com.hheimerd.hangouts.screens

import androidx.compose.runtime.Composable
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
fun CreateContactScreen(
    contactsViewModel: ContactsViewModel,
    onUiEvent: ActionWith<UiEvent>,
    modifier: Modifier = Modifier
) {
    EditContact(
        onSave = { contact ->
            contactsViewModel.onEvent(ContactEvent.Insert(contact))
        },
        initialValue = Contact("", ""),
        title = stringResource(id = R.string.create_contact_title),
        onOpenSettingsClick = {onUiEvent(UiEvent.Navigate(Routes.Settings))},
        onClose = {onUiEvent(UiEvent.PopBack)},
        modifier = modifier
    )
}
