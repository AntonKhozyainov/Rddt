package ru.khozyainov.rddt.ui.launcher

sealed class LauncherState {
    object NavigateToOnboardingAction : LauncherState()
    object NavigateToLoginActivityAction : LauncherState()
    object NavigateToMainActivityAction : LauncherState()
    object Loading : LauncherState()
    class Error(val exception: Throwable) : LauncherState()
}