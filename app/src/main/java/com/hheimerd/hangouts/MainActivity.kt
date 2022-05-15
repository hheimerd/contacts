package com.hheimerd.hangouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hheimerd.hangouts.components.SearchTopAppBar
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HangoutsTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val vm = viewModel<ContactsViewModel>()
    val contacts = vm.getAllContacts().collectAsState(listOf()).value

    MainScreenContent(contacts)
}

@Composable
fun MainScreenContent(contacts: List<Contact>) {
    val scaffoldState = rememberScaffoldState()
    val searchValue = rememberSaveable { mutableStateOf("") }

    HangoutsTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                SearchTopAppBar(
                    searchValue.value,
                    onSearchChanged = { searchValue.value = it }
                )
            },
            backgroundColor = MaterialTheme.colors.background
        ) {

        }
    }
}


@Preview
@Composable
fun SearchPreview() {
    HangoutsTheme(true) {
        Scaffold(
            topBar = {
                SearchTopAppBar("", {})
            },
            backgroundColor = MaterialTheme.colors.background
        ) {

        }
    }
}
