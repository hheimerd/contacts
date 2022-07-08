package com.hheimerd.hangouts.utils.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hheimerd.hangouts.utils.typeUtils.Action
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun ViewModel.runInIOThread(action: suspend () -> Unit, onCompleted: (suspend () -> Unit)?) {
    viewModelScope.launch(Dispatchers.IO) {
        action()
        viewModelScope.launch(Dispatchers.Main) {
            onCompleted?.invoke()
        }
    }
}