package com.hheimerd.hangouts.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hheimerd.hangouts.viewModels.ContactsViewModel
import com.hheimerd.hangouts.components.ContactsListView
import com.hheimerd.hangouts.components.SearchTopAppBar
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.getRandomString
import java.util.*


@Composable
fun MainScreen() {
    val vm = viewModel<ContactsViewModel>()
    val contacts = vm.getAllContacts().collectAsState(listOf()).value


    HangoutsTheme {
        MainScreenContent(contacts)
    }
}

@Composable
fun MainScreenContent(contacts: List<Contact>, modifier: Modifier = Modifier) {
    val scaffoldState = rememberScaffoldState()
    val searchValue = rememberSaveable { mutableStateOf("") }
    val grouped = remember(contacts, searchValue.value) {
        contacts
            .filter {
                it.name.lowercase().contains(searchValue.value.lowercase(), true) ||
                        it.secondName.contains(searchValue.value, true) ||
                        it.phone.contains(searchValue.value, true)
            }
            .groupBy { it.name.first().uppercaseChar() }
            .toSortedMap()
    }


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SearchTopAppBar(
                searchValue.value,
                onSearchChanged = { searchValue.value = it },
                onAddContactClick = {

                }
            )
        },
        backgroundColor = MaterialTheme.colors.background,
        modifier = Modifier
            .background(MaterialTheme.colors.background)
            .fillMaxSize()
            .statusBarsPadding()
            .windowInsetsPadding(
                WindowInsets
                    .navigationBars
                    .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top)
            ),
        content = {
            ContactsListView(
                grouped,
                modifier
                    .padding(it)
            )
        }
    )
}


val testContact = Contact("+79152152125", "Name", "Surname");


@Preview(showSystemUi = true)
@Composable
fun PreviewMainScreenContent() {
    val contacts = List(25) {
        testContact.copy(
            name = getRandomString((6..10).random()),
            id = UUID.randomUUID().toString()
        )
    }

    HangoutsTheme(true) {
        MainScreenContent(contacts)
    }
}