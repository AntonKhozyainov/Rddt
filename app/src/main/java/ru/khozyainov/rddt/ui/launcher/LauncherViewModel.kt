package ru.khozyainov.rddt.ui.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import ru.khozyainov.domain.usecase.login.GetLoginStateUseCase
import ru.khozyainov.domain.usecase.onboarding.GetOnboardingStateUseCase

@OptIn(ExperimentalCoroutinesApi::class)
class LauncherViewModel(
    getOnboardingStateUseCase: GetOnboardingStateUseCase,
    getLoginStateUseCase: GetLoginStateUseCase
) : ViewModel() {

    private val uiMutableState =
        MutableStateFlow<LauncherNavigationAction>(LauncherNavigationAction.Loading)
    val uiState: StateFlow<LauncherNavigationAction> = uiMutableState

    private val onboardingViewed = getOnboardingStateUseCase()
        .mapLatest { onboarding ->
            onboarding.viewed
        }.catch { exception ->
            uiMutableState.value = LauncherNavigationAction.Error(exception)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    private val loginCompleted = getLoginStateUseCase()
        .mapLatest { token ->
            token.accessToken.isNotEmpty()
        }.catch { exception ->
            uiMutableState.value = LauncherNavigationAction.Error(exception)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = null
        )

    init {
        refresh()
    }

    fun refresh() {
        combine(
            onboardingViewed, loginCompleted
        ) { onboarding, login ->
            onboarding to login
        }.onEach { (onboardingViewed, loginCompleted) ->
            uiMutableState.value =
                getNavAction(onboardingViewed = onboardingViewed, loginCompleted = loginCompleted)
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = LauncherNavigationAction.Loading
        )
    }

    private fun getNavAction(
        onboardingViewed: Boolean?,
        loginCompleted: Boolean?
    ): LauncherNavigationAction {
        return if (onboardingViewed != null && loginCompleted != null) {
            when {
                onboardingViewed && loginCompleted -> LauncherNavigationAction.NavigateToMainActivityAction
                onboardingViewed && !loginCompleted -> LauncherNavigationAction.NavigateToLoginActivityAction
                else -> LauncherNavigationAction.NavigateToOnboardingAction
            }
        } else {
            LauncherNavigationAction.Loading
        }
    }

}