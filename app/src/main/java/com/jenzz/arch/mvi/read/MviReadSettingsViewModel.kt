package com.jenzz.arch.mvi.read

import androidx.compose.runtime.State
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jenzz.arch.mvi.*
import com.jenzz.arch.shared.AnalyticsManager
import com.jenzz.arch.shared.AnalyticsPage
import com.jenzz.arch.shared.Settings
import com.jenzz.arch.shared.SettingsRepository
import kotlinx.coroutines.launch

class MviReadSettingsViewModel(
    private val store: Store<MviSettingsViewState, MviReadSettingsAction>,
) : ViewModel() {

    val state: State<MviSettingsViewState> = store.state

    init {
        fetchSettings()
    }

    private fun fetchSettings() {
        viewModelScope.launch {
            store.dispatch(MviReadSettingsAction.FetchSettings)
        }
    }
}

sealed class MviReadSettingsAction : Action {

    object FetchSettings : MviReadSettingsAction()

    object FetchingSettings : MviReadSettingsAction()

    data class FetchedSettings(val settings: Settings) : MviReadSettingsAction()
}

class MviReadSettingsStore : BaseStore<MviSettingsViewState, MviReadSettingsAction>(
    initialState = MviSettingsViewState(),
    reducer = MviReadSettingsReducer(),
    middlewares = listOf(
        MviReadSettingsDataMiddleware(SettingsRepository()),
        MviReadSettingsAnalyticsMiddleware(AnalyticsManager()),
    )
)

class MviReadSettingsReducer : Reducer<MviSettingsViewState, MviReadSettingsAction> {

    override fun reduce(
        state: MviSettingsViewState,
        action: MviReadSettingsAction,
    ): MviSettingsViewState =
        when (action) {
            is MviReadSettingsAction.FetchSettings ->
                state // no-op
            is MviReadSettingsAction.FetchingSettings ->
                state.copy(isLoading = true)
            is MviReadSettingsAction.FetchedSettings ->
                state.copy(
                    isLoading = false,
                    isPreference1Enabled = action.settings.isPreference1Enabled,
                    isPreference2Enabled = action.settings.isPreference2Enabled,
                )
        }
}

class MviReadSettingsDataMiddleware(
    private val repository: SettingsRepository,
) : Middleware<MviSettingsViewState, MviReadSettingsAction> {

    override suspend fun process(
        state: MviSettingsViewState,
        action: MviReadSettingsAction,
        store: Store<MviSettingsViewState, MviReadSettingsAction>,
    ) {
        when (action) {
            is MviReadSettingsAction.FetchSettings ->
                fetchSettings(store)
            is MviReadSettingsAction.FetchingSettings ->
                Unit // no-op
            is MviReadSettingsAction.FetchedSettings ->
                Unit // no-op
        }
    }

    private suspend fun fetchSettings(
        store: Store<MviSettingsViewState, MviReadSettingsAction>,
    ) {
        store.dispatch(MviReadSettingsAction.FetchingSettings)
        val settings = repository.fetchSettings()
        store.dispatch(MviReadSettingsAction.FetchedSettings(settings))
    }
}

class MviReadSettingsAnalyticsMiddleware(
    private val analyticsManager: AnalyticsManager,
) : Middleware<MviSettingsViewState, MviReadSettingsAction> {

    override suspend fun process(
        state: MviSettingsViewState,
        action: MviReadSettingsAction,
        store: Store<MviSettingsViewState, MviReadSettingsAction>,
    ) {
        when (action) {
            is MviReadSettingsAction.FetchSettings ->
                trackPageView()
            is MviReadSettingsAction.FetchingSettings ->
                Unit // no-op
            is MviReadSettingsAction.FetchedSettings ->
                Unit // no-op
        }
    }

    private fun trackPageView() {
        analyticsManager.trackPageView(AnalyticsPage.Main)
    }
}
