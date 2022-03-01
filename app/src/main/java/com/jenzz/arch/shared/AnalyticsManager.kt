package com.jenzz.arch.shared

@Suppress("UNUSED_PARAMETER")
class AnalyticsManager {

    fun trackPageView(page: AnalyticsPage) = Unit

    fun trackEvent(enabled: AnalyticsEvent) = Unit
}

enum class AnalyticsPage {
    Main,
}

sealed class AnalyticsEvent {
    data class Preference1Change(val enabled: Boolean) : AnalyticsEvent()
    data class Preference2Change(val enabled: Boolean) : AnalyticsEvent()
}
