package com.jenzz.arch.mvi.update

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jenzz.arch.mvi.MviSettingsScreen

@Composable
fun MviUpdateSettingsScreen(
    viewModel: MviUpdateSettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MviUpdateSettingsViewModel(
                    store = MviUpdateSettingsStore()
                ) as T
        }
    ),
) {
    MviSettingsScreen(
        state = viewModel.state.value,
        onPreference1Change = viewModel::onPreference1Change,
        onPreference2Change = viewModel::onPreference2Change,
    )
}
