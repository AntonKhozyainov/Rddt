package ru.khozyainov.rddt.ui.launcher

sealed class LauncherState {
    object NavigateToOnboardingAction : LauncherState()
    object NavigateToLoginAction : LauncherState()
    object NavigateToMainActivityAction : LauncherState()
    data class Error(val exception: Throwable) : LauncherState()
    object Default : LauncherState()
}