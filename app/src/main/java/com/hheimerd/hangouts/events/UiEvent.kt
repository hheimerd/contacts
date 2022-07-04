package com.hheimerd.hangouts.events

import android.widget.Toast
import androidx.compose.material.SnackbarDuration
import com.hheimerd.hangouts.ui.StringResource

sealed class UiEvent {
    object PopBack: UiEvent();
    data class Navigate(val routeString: String, val withPopAll: Boolean = false): UiEvent();

    data class RequestPermission(val permission: String, val onResult: (Boolean) -> Unit): UiEvent()

    data class ShowSnackbar(
        val message: StringResource,
        val actionMessage: StringResource? = null,
        val duration: SnackbarDuration = SnackbarDuration.Long,
        val action: () -> Unit = {},
    ): UiEvent();

    data class ShowToast(
        val message: StringResource,
        val length: Int = Toast.LENGTH_SHORT
    ): UiEvent();


}