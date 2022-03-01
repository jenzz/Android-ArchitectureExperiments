package com.jenzz.arch.mvi.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenzz.arch.mvi.*
import com.jenzz.arch.shared.*
import kotlinx.coroutines.launch

class MviUpdateSettingsViewModel(
    private val store: Store<MviSettingsViewState, MviUpdateSettingsAction>,
) : ViewModel() {

    val state = store.state

    init {
        fetchSettings()
    }

    private fun fetchSettings() {
        viewModelScope.launch {
            store.dispatch(MviUpdateSettingsAction.FetchSettings)
        }
    }

    fun onPreference1Change(enabled: Boolean) {
        viewModelScope.launch {
            store.dispatch(MviUpdateSettingsAction.UpdatePreference1(enabled))
        }
    }

    fun onPreference2Change(enabled: Boolean) {
        viewModelScope.launch {
            store.dispatch(MviUpdateSettingsAction.UpdatePreference2(enabled))
        }
    }
}

sealed class MviUpdateSettingsAction : Action {

    object FetchSettings : MviUpdateSettingsAction()

    object FetchingSettings : MviUpdateSettingsAction()

    data class FetchedSettings(val settings: Settings) : MviUpdateSettingsAction()

    data class UpdatePreference1(val enabled: Boolean) : MviUpdateSettingsAction()

    data class UpdatePreference2(val enabled: Boolean) : MviUpdateSettingsAction()
}

class MviUpdateSettingsStore : BaseStore<MviSettingsViewState, MviUpdateSettingsAction>(
    initialState = MviSettingsViewState(),
    reducer = MviUpdateSettingsReducer(),
    middlewares = listOf(
        MviUpdateSettingsDataMiddleware(SettingsRepository()),
        MviUpdateSettingsAnalyticsMiddleware(AnalyticsManager()),
    )
)

class MviUpdateSettingsReducer : Reducer<MviSettingsViewState, MviUpdateSettingsAction> {

    override fun reduce(
        state: MviSettingsViewState,
        action: MviUpdateSettingsAction,
    ): MviSettingsViewState =
        when (action) {
            is MviUpdateSettingsAction.FetchSettings ->
                state // no-op
            is MviUpdateSettingsAction.FetchingSettings ->
                state.copy(isLoading = true)
            is MviUpdateSettingsAction.FetchedSettings ->
                state.copy(
                    isLoading = false,
                    isPreference1Enabled = action.settings.isPreference1Enabled,
                    isPreference2Enabled = action.settings.isPreference2Enabled,
                )
            is MviUpdateSettingsAction.UpdatePreference1 ->
                state // no-op
            is MviUpdateSettingsAction.UpdatePreference2 ->
                state // no-op
        }
}

class MviUpdateSettingsDataMiddleware(
    private val repository: SettingsRepository,
) : Middleware<MviSettingsViewState, MviUpdateSettingsAction> {

    override suspend fun process(
        state: MviSettingsViewState,
        action: MviUpdateSettingsAction,
        store: Store<MviSettingsViewState, MviUpdateSettingsAction>,
    ) {
        when (action) {
            is MviUpdateSettingsAction.FetchSettings ->
                fetchSettings(store)
            is MviUpdateSettingsAction.FetchingSettings ->
                Unit // no-op
            is MviUpdateSettingsAction.FetchedSettings ->
                Unit // no-op
            is MviUpdateSettingsAction.UpdatePreference1 ->
                updatePreference1(action.enabled, store)
            is MviUpdateSettingsAction.UpdatePreference2 ->
                updatePreference2(action.enabled, store)
        }
    }

    private suspend fun fetchSettings(
        store: Store<MviSettingsViewState, MviUpdateSettingsAction>,
    ) {
        store.dispatch(MviUpdateSettingsAction.FetchingSettings)
        val settings = repository.fetchSettings()
        store.dispatch(MviUpdateSettingsAction.FetchedSettings(settings))
    }

    private suspend fun updatePreference1(
        enabled: Boolean,
        store: Store<MviSettingsViewState, MviUpdateSettingsAction>,
    ) {
        val newSettings = repository.updatePreference1(enabled)
        store.dispatch(MviUpdateSettingsAction.FetchedSettings(newSettings))
    }

    private suspend fun updatePreference2(
        enabled: Boolean,
        store: Store<MviSettingsViewState, MviUpdateSettingsAction>,
    ) {
        val newSettings = repository.updatePreference2(enabled)
        store.dispatch(MviUpdateSettingsAction.FetchedSettings(newSettings))
    }
}

class MviUpdateSettingsAnalyticsMiddleware(
    private val analyticsManager: AnalyticsManager,
) : Middleware<MviSettingsViewState, MviUpdateSettingsAction> {

    override suspend fun process(
        state: MviSettingsViewState,
        action: MviUpdateSettingsAction,
        store: Store<MviSettingsViewState, MviUpdateSettingsAction>,
    ) {
        when (action) {
            is MviUpdateSettingsAction.FetchSettings ->
                trackPageView()
            is MviUpdateSettingsAction.FetchingSettings ->
                Unit // no-op
            is MviUpdateSettingsAction.FetchedSettings ->
                Unit // no-op
            is MviUpdateSettingsAction.UpdatePreference1 ->
                trackPreference1Change(action.enabled)
            is MviUpdateSettingsAction.UpdatePreference2 ->
                trackPreference2Change(action.enabled)
        }
    }

    private fun trackPageView() {
        analyticsManager.trackPageView(AnalyticsPage.Main)
    }

    private fun trackPreference1Change(enabled: Boolean) {
        analyticsManager.trackEvent(AnalyticsEvent.Preference1Change(enabled))
    }

    private fun trackPreference2Change(enabled: Boolean) {
        analyticsManager.trackEvent(AnalyticsEvent.Preference2Change(enabled))
    }
}
