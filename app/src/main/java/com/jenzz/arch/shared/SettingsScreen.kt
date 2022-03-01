package com.jenzz.arch.shared

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun SettingsScreen(
    state: SettingsViewState,
    onPreference1Change: (Boolean) -> Unit,
    onPreference2Change: (Boolean) -> Unit,
) {
    if (state.isLoading) {
        CircularProgressIndicator()
    } else {
        SettingsContent(
            state = state,
            onPreference1Change = onPreference1Change,
            onPreference2Change = onPreference2Change
        )
    }
}

@Composable
private fun SettingsContent(
    state: SettingsViewState,
    onPreference1Change: (Boolean) -> Unit,
    onPreference2Change: (Boolean) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Row {
            Checkbox(
                checked = state.isPreference1Enabled,
                onCheckedChange = { checked -> onPreference1Change(checked) },
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Preference 1",
            )
        }
        Row {
            Checkbox(
                checked = state.isPreference2Enabled,
                onCheckedChange = { checked -> onPreference2Change(checked) },
            )
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Preference 2",
            )
        }
    }
}
