package com.jenzz.arch.mvvm.update

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jenzz.arch.shared.AnalyticsManager
import com.jenzz.arch.shared.SettingsRepository
import com.jenzz.arch.shared.SettingsScreen

@Composable
fun MvvmUpdateSettingsScreen(
    viewModel: MvvmUpdateSettingsViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MvvmUpdateSettingsViewModel(
                    settingsRepository = SettingsRepository(),
                    analyticsManager = AnalyticsManager(),
                ) as T
        }
    ),
) {
    SettingsScreen(
        state = viewModel.state,
        onPreference1Change = viewModel::onPreference1Change,
        onPreference2Change = viewModel::onPreference2Change,
    )
}
