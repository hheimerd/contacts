package com.hheimerd.hangouts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import com.hheimerd.hangouts.components.SearchTopAppBar
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.getRandomString
import com.hheimerd.hangouts.utils.rememberColorByString
import com.hheimerd.hangouts.utils.typeUtils.Action
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactsListView(contacts: Map<Char, List<Contact>>) {
    LazyColumn {
        contacts.forEach { (firstLetter, contacts) ->
            stickyHeader(key = firstLetter) {
                Text(
                    text = firstLetter.toString(),
                    modifier = Modifier
                        .height(1.dp)
                        .requiredHeight(30.dp)
                        .padding(start = 20.dp)
                        .offset(y = (26).dp),
                    fontSize = 20.sp
                )
            }
            items(contacts, key = { it.id }) {
                ContactListItem(
                    contact = it, modifier = Modifier
                        .padding(start = 50.dp, end = 10.dp)
                        .clickable { }
                        .padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
fun ContactListItem(contact: Contact, modifier: Modifier = Modifier) {
    val color = rememberColorByString(contact.name)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .then(modifier),
        verticalAlignment = Alignment.CenterVertically
    ) {
        contact.run {
            if (imageUri.isNullOrEmpty())
                AvatarDefault(name.first().uppercaseChar(), color)
            else
                Image(rememberImagePainter(imageUri), name)
            Spacer(modifier = Modifier.width(20.dp))
            Text(
                "$name $secondName",
                fontSize = 20.sp,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun AvatarDefault(letter: Char, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .requiredSize(40.dp)
            .drawBehind {
                drawCircle(color)
            }
            .then(modifier),
    ) {
        Text(
            letter.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .padding(top = 6.dp)
                .fillMaxSize(),
            textAlign = TextAlign.Center,

            )
    }
}

val testContact = Contact("+79152152125", "Name", "Surname");

@Preview(showBackground = true)
@Composable
fun PreviewContactListItem() {
    ContactListItem(testContact, modifier = Modifier.padding(start = 60.dp))
}


@Preview
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
