package com.hheimerd.hangouts.ui.add_edit_contact

import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.events.UiEvent
import com.hheimerd.hangouts.utils.typeUtils.Action

@Composable
fun AddEditContactScreen(
    contactsViewModel: AddEditContactViewModel = hiltViewModel(),
    modifier: Modifier = Modifier
) {
    val scaffoldState = rememberScaffoldState()
    val context = LocalContext.current
    LaunchedEffect(true) {
        contactsViewModel.uiEvent.collect { event ->
            if (event is UiEvent.ShowSnackbar) {
                scaffoldState.snackbarHostState.showSnackbar(
                    event.message.asString(context),
                    event.action?.asString(context),
                    event.duration
                );
            }
        }
    }

    val titleId =
        if (contactsViewModel.initialContact == null) R.string.create_contact_title else R.string.edit_contact_title

    EditContact(
        contactsViewModel::onEvent,
        contactsViewModel.initialContact ?: Contact("", ""),
        scaffoldState,
        title = stringResource(titleId),
        modifier = modifier
    )
}
