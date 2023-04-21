package ru.khozyainov.rddt.ui.launcher

sealed class LauncherNavigationAction{
    object NavigateToOnboardingAction : LauncherNavigationAction()
    object NavigateToLoginActivityAction : LauncherNavigationAction()
    object NavigateToMainActivityAction : LauncherNavigationAction()
    object Loading: LauncherNavigationAction()
    class Error(val exception: Throwable) : LauncherNavigationAction()
}