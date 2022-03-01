package com.jenzz.arch.mvi

data class MviSettingsViewState(
    val isLoading: Boolean = false,
    val isPreference1Enabled: Boolean = false,
    val isPreference2Enabled: Boolean = false,
) : State
