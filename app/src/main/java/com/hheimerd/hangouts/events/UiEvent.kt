package com.hheimerd.hangouts.events

import androidx.compose.material.SnackbarDuration
import com.hheimerd.hangouts.ui.StringResource

sealed class UiEvent {
    object PopBack: UiEvent();
    data class Navigate(val routeString: String, val withPopAll: Boolean = false): UiEvent();
    data class ShowSnackbar(
        val message: StringResource,
        val action: StringResource? = null,
        val duration: SnackbarDuration = SnackbarDuration.Long,
    ): UiEvent();


}