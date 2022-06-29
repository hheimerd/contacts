package com.hheimerd.hangouts.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.data.models.Contact
import com.hheimerd.hangouts.ui.contact_card.ContactCardEvent
import com.hheimerd.hangouts.ui.styles.topAppBarPadding
import com.hheimerd.hangouts.utils.typeUtils.ActionWith

@Composable
fun ContactCardView(
    contact: Contact,
    onEvent: ActionWith<ContactCardEvent>
) {
    Scaffold(
        topBar = { ContactCardTopBar(onEvent) }
    ) { scaffoldPaddings ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(scaffoldPaddings)
        ) {
            
            Text(text = contact.firstName)

        }
    }

}

@Composable
fun ContactCardTopBar(onEvent: ActionWith<ContactCardEvent>) {
    var navExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        modifier = Modifier
            .topAppBarPadding(),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        // Back
        IconButton(onClick = { onEvent(ContactCardEvent.BackButtonClicked) }) {
            Icon(Icons.Outlined.ArrowBack, stringResource(R.string.back))
        }
        Spacer(Modifier.weight(1f))

        // Edit
        IconButton(onClick = { onEvent(ContactCardEvent.EditContactClick) }) {
            Icon(Icons.Outlined.Edit, stringResource(R.string.edit))
        }

        DropdownMenu(
            expanded = navExpanded,
            onDismissRequest = { navExpanded = false },
            ) {
            DropdownMenuItem(onClick = { onEvent(ContactCardEvent.DeleteContactClick) }) {
                Text(stringResource(R.string.delete))
            }
        }
        IconButton(onClick = { navExpanded = !navExpanded }) {
            Icon(Icons.Outlined.MoreVert, stringResource(R.string.more))
        }
    }
}