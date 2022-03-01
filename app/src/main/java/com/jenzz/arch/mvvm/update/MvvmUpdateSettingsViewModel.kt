package com.jenzz.arch.mvvm.update

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenzz.arch.shared.*
import kotlinx.coroutines.launch

class MvvmUpdateSettingsViewModel(
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

    fun onPreference1Change(enabled: Boolean) {
        viewModelScope.launch {
            val newSettings = settingsRepository.updatePreference1(enabled)

            state = state.copy(isPreference1Enabled = newSettings.isPreference1Enabled)
        }

        analyticsManager.trackEvent(AnalyticsEvent.Preference1Change(enabled))
    }

    fun onPreference2Change(enabled: Boolean) {
        viewModelScope.launch {
            val newSettings = settingsRepository.updatePreference2(enabled)

            state = state.copy(isPreference2Enabled = newSettings.isPreference2Enabled)
        }

        analyticsManager.trackEvent(AnalyticsEvent.Preference2Change(enabled))
    }
}
