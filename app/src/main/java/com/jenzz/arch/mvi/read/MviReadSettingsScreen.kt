package com.jenzz.arch.mvi.read

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jenzz.arch.mvi.MviSettingsScreen

@Composable
fun MviReadSettingsScreen(
    viewModel: MviReadSettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MviReadSettingsViewModel(
                    store = MviReadSettingsStore()
                ) as T
        }
    ),
) {
    MviSettingsScreen(
        state = viewModel.state.value,
        onPreference1Change = { },
        onPreference2Change = { },
    )
}
