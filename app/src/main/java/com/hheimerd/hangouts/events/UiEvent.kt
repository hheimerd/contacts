package com.hheimerd.hangouts.events

sealed class UiEvent {
    object PopBack: UiEvent();
    data class Navigate(val routeString: String): UiEvent();
    data class ShowSnackbar(
        val message: String,
        val action: String? = null
    ): UiEvent();


}