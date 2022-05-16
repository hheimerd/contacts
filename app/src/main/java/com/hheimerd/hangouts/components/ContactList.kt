package com.hheimerd.hangouts.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.styles.avatarSize
import com.hheimerd.hangouts.styles.avatarSpace
import com.hheimerd.hangouts.testContact
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.getRandomString
import com.hheimerd.hangouts.utils.rememberColorByString
import com.hheimerd.hangouts.utils.typeUtils.Action
import com.hheimerd.hangouts.utils.typeUtils.ActionWith
import java.util.*

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ContactsListView(
    contacts: Map<Char, List<Contact>>,
    modifier: Modifier = Modifier,
    onContactClick: ActionWith<Contact> = {},
    onCreateContactClick: Action = {}
) {
    LazyColumn(modifier = Modifier.padding(top = 10.dp).then(modifier)) {
        item {
            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(start = 50.dp, end = 10.dp)
                .clickable { onCreateContactClick() }
                .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(modifier = Modifier.avatarSize()) {
                    Icon(
                        Icons.Outlined.Person,
                        stringResource(id = R.string.add_contact),
                        modifier = Modifier
                            .offset(5.dp, 7.dp)
                            .size(25.dp)
                    )
                    Icon(
                        Icons.Rounded.Add,
                        stringResource(id = R.string.add_contact),
                        modifier = Modifier
                            .offset(20.dp, 10.dp)
                            .size(15.dp)
                    )
                }

                Spacer(modifier = Modifier.avatarSpace())

                Text(text = stringResource(id = R.string.create_contact))
            }
        }

        contacts.forEach { (firstLetter, contacts) ->
            stickyHeader(key = firstLetter) {
                Text(
                    text = firstLetter.toString(),
                    modifier = Modifier
                        .height(1.dp)
                        .requiredHeight(30.dp)
                        .padding(start = 20.dp)
                        .offset(y = (26).dp),
                    style = MaterialTheme.typography.body1
                )
            }
            items(contacts, key = { it.id }) {
                ContactListItem(
                    contact = it, modifier = Modifier
                        .padding(start = 50.dp, end = 10.dp)
                        .clickable { onContactClick(it) }
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
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        contact.run {
            if (imageUri.isNullOrEmpty())
                AvatarDefault(contact, color)
            else
                Avatar(imageUri = imageUri, name)
            Spacer(modifier = Modifier.avatarSpace())
            Text(
                "$name $secondName",
                style = MaterialTheme.typography.body1
            )
        }
    }
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

    val grouped = remember(contacts) {
        contacts
            .groupBy { it.name.first().uppercaseChar() }
            .toSortedMap()
    }
    HangoutsTheme(true) {
        ContactsListView(grouped)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewContactListItem() {
    ContactListItem(testContact, modifier = Modifier.padding(start = 60.dp))
}