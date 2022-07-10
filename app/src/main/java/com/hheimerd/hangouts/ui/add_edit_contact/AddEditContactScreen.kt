package com.hheimerd.hangouts.ui.add_edit_contact

import androidx.compose.material.SnackbarResult
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.events.UiEvent

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
                val result = scaffoldState.snackbarHostState.showSnackbar(
                    event.message.asString(context),
                    event.actionMessage?.asString(context),
                    event.duration
                );
                if (result == SnackbarResult.ActionPerformed)
                    event.action()
            }
        }
    }

    val titleId =
        if (contactsViewModel.state?.hasInitialContact == false) R.string.create_contact_title
        else R.string.edit_contact_title



    AddEditContactView(
        contactsViewModel::onEvent,
        contactsViewModel.state ?: AddEditContactStateHost(),
        scaffoldState,
        title = stringResource(titleId),
        modifier = modifier
    )
}
