package com.hheimerd.hangouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hheimerd.hangouts.components.ContactsListView
import com.hheimerd.hangouts.components.SearchTopAppBar
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.getRandomString
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
//            val systemUiController = rememberSystemUiController()
//            systemUiController.setSystemBarsColor(Color.Transparent,
//                darkIcons = false)

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


    HangoutsTheme {
        MainScreenContent(contacts)
    }
}

@Composable
fun MainScreenContent(contacts: List<Contact>) {
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
        backgroundColor = MaterialTheme.colors.background
    ) {
        ContactsListView(grouped)
    }
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
