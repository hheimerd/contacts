package com.hheimerd.hangouts.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hheimerd.hangouts.R
import com.hheimerd.hangouts.components.EditContact
import com.hheimerd.hangouts.models.Contact
import com.hheimerd.hangouts.styles.paddingSm
import com.hheimerd.hangouts.ui.theme.HangoutsTheme
import com.hheimerd.hangouts.utils.typeUtils.Action
import com.hheimerd.hangouts.utils.typeUtils.ActionWith
import com.hheimerd.hangouts.viewModels.ContactsViewModel

@Composable
fun ShowContactScreen(
    contactsViewModel: ContactsViewModel = viewModel(),
    onOpenSettingsClick: Action,
) {

}