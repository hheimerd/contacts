package com.hheimerd.hangouts.ui.chat

import android.content.Context
import com.hheimerd.hangouts.data.models.ChatMessage

sealed class ChatEvent {
    data class SendMessage(val message: String, val context: Context): ChatEvent()

    object GoBack: ChatEvent()
    object RemoveChat: ChatEvent()
}