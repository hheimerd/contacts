package com.hheimerd.hangouts.ui.contact_card

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.hheimerd.hangouts.components.ContactCardView
import com.hheimerd.hangouts.data.models.Contact

@Composable
fun ContactCardScreen(
    viewModel: ContactCardViewModel = hiltViewModel()
) {
    if (viewModel.contact == null)
        return;
    ContactCardView(contact = viewModel.contact!!, onEvent = viewModel::onEvent)
}