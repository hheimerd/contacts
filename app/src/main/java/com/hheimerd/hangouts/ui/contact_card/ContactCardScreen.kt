package com.hheimerd.hangouts.ui.contact_card

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ContactCardScreen(
    viewModel: ContactCardViewModel = hiltViewModel()
) {
    if (viewModel.contact == null)
        return
    ContactCardView(contact = viewModel.contact!!, onEvent = viewModel::onEvent)
}