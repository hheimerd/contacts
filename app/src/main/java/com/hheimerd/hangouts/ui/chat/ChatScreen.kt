package com.hheimerd.hangouts.ui.chat

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun ChatScreen(
    viewModel: ChatViewModel = hiltViewModel()
) {
    if (viewModel.contact == null) return

    ChatView(viewModel.messages, viewModel.contact!!, viewModel::onEvent)
}