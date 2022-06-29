package com.hheimerd.hangouts.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hheimerd.hangouts.ui.main_screen.MainScreenViewModel
import com.hheimerd.hangouts.components.ContactsListView
import com.hheimerd.hangouts.components.SearchTopAppBar
import com.hheimerd.hangouts.ui.main_screen.MainScreenEvent
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.getRandomString
import com.hheimerd.hangouts.utils.typeUtils.ActionWith
import java.util.*


@Composable
fun MainScreen(
    viewModel: MainScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val contacts = viewModel.getAllContacts().collectAsState(listOf()).value

    MainScreenContent(
        contacts,
        viewModel::onEvent,
        modifier = modifier,
    )
}

@Composable
fun MainScreenContent(
    contacts: List<Contact>,
    onMainScreenEvent: ActionWith<MainScreenEvent>,
    modifier: Modifier = Modifier,
) {
    val scaffoldState = rememberScaffoldState()
    val searchValue = rememberSaveable { mutableStateOf("") }
    val grouped = remember(contacts, searchValue.value) {
        contacts
            .filter {
                it.firstName.lowercase().contains(searchValue.value.lowercase(), true) ||
                        it.lastName.contains(searchValue.value, true) ||
                        it.phone.contains(searchValue.value, true)
            }
            .groupBy { it.firstName.first().uppercaseChar() }
            .toSortedMap()
    }


    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            SearchTopAppBar(
                searchValue.value,
                onSearchChanged = { searchValue.value = it },
                onAddContactClick = {onMainScreenEvent(MainScreenEvent.AddContactClick)},
                onOpenSettingsClick = {onMainScreenEvent(MainScreenEvent.OpenSettings)}
            )
        },
        backgroundColor = MaterialTheme.colors.background,
        modifier = modifier,
        content = {
            ContactsListView(
                grouped,
                onMainScreenEvent = onMainScreenEvent,
                modifier = modifier
                    .padding(it),
            )
        }
    )
}


val testContact = Contact("+79152152125", "Name", "Surname")


@Preview(showSystemUi = true)
@Composable
fun PreviewMainScreenContent() {
    val contacts = List(25) {
        testContact.copy(
            firstName = getRandomString((6..10).random()),
            id = UUID.randomUUID().toString()
        )
    }

    HangoutsTheme(true) {
        MainScreenContent(contacts, {})
    }
}