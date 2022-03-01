package com.jenzz.arch.shared

class SettingsRepository {

    private var settings = Settings(
        isPreference1Enabled = false,
        isPreference2Enabled = false,
    )

    suspend fun fetchSettings(): Settings =
        settings

    suspend fun updatePreference1(enabled: Boolean): Settings {
        settings = settings.copy(isPreference1Enabled = enabled)
        return settings
    }

    suspend fun updatePreference2(enabled: Boolean): Settings {
        settings = settings.copy(isPreference2Enabled = enabled)
        return settings
    }
}
