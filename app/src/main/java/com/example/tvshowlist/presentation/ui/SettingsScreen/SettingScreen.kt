package com.example.tvshowlist.presentation.ui.SettingsScreen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.tvshowlist.R
import com.example.tvshowlist.presentation.MainViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    viewModel: SettingsViewModel = koinViewModel(),
    navigateToHome: () -> Unit,
    onCensorPotentialSpoilerContentChange: (Boolean) -> Unit,
    censorContent: Boolean
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.settings))
                },
            )
        }
    ) { paddingValues ->

        ListItem(
            headlineContent = {
                Text(
                    stringResource(R.string.censor_potential_spoiler_content),
                    modifier = Modifier.padding(paddingValues = paddingValues)
                )
            },
            trailingContent = {
                Checkbox(
                    checked = censorContent,
                    onCheckedChange = onCensorPotentialSpoilerContentChange,
                    modifier = Modifier.padding(paddingValues = paddingValues)
                )
            }
        )
    }
}
