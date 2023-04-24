package ru.khozyainov.rddt.ui.onboarding

sealed class OnboardingState{
    object Success: OnboardingState()
    object Default: OnboardingState()
    data class Error(val exception: Throwable): OnboardingState()
}
