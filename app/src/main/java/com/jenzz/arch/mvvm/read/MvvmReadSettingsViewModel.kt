package com.jenzz.arch.mvvm.read

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenzz.arch.shared.AnalyticsManager
import com.jenzz.arch.shared.AnalyticsPage
import com.jenzz.arch.shared.SettingsRepository
import com.jenzz.arch.shared.SettingsViewState
import kotlinx.coroutines.launch

class MvvmReadSettingsViewModel(
    private val settingsRepository: SettingsRepository,
    private val analyticsManager: AnalyticsManager,
) : ViewModel() {

    var state by mutableStateOf(SettingsViewState())
        private set

    init {
        fetchSettings()
        trackPageView()
    }

    private fun fetchSettings() {
        viewModelScope.launch {
            state = state.copy(isLoading = true)

            val settings = settingsRepository.fetchSettings()

            state = SettingsViewState(
                isLoading = false,
                isPreference1Enabled = settings.isPreference1Enabled,
                isPreference2Enabled = settings.isPreference2Enabled,
            )
        }
    }

    private fun trackPageView() {
        analyticsManager.trackPageView(AnalyticsPage.Main)
    }
}
