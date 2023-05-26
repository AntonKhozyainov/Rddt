package ru.khozyainov.rddt.ui.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.khozyainov.domain.usecase.login.GetLoginStateUseCase
import ru.khozyainov.domain.usecase.onboarding.GetOnboardingStateUseCase

class LauncherViewModel(
    private val getOnboardingStateUseCase: GetOnboardingStateUseCase,
    private val getLoginStateUseCase: GetLoginStateUseCase
) : ViewModel() {

    private val uiMutableState = MutableStateFlow<LauncherState>(LauncherState.Default)
    val uiState: StateFlow<LauncherState> = uiMutableState

    private var job: Job? = null

    init {
        refresh()
    }

    fun refresh() {
        job?.cancel()
        job = combine(
            getOnboardingStateUseCase(), getLoginStateUseCase()
        ) { onboarding, login ->
            onboarding to login
        }.onEach { (onboarding, login) ->
            uiMutableState.value = getNavAction(
                onboardingViewed = onboarding.viewed,
                loginCompleted = login.accessToken.isNotEmpty()
            )
        }.flowOn(Dispatchers.IO).catch { exception ->
            uiMutableState.value = LauncherState.Error(exception)
        }.launchIn(viewModelScope)
    }

    private fun getNavAction(
        onboardingViewed: Boolean?, loginCompleted: Boolean?
    ): LauncherState {
        return if (onboardingViewed != null && loginCompleted != null) {
            when {
                onboardingViewed && loginCompleted -> LauncherState.NavigateToMainActivityAction
                onboardingViewed && !loginCompleted -> LauncherState.NavigateToLoginAction
                else -> LauncherState.NavigateToOnboardingAction
            }
        } else {
            LauncherState.Default
        }
    }

    override fun onCleared() {
        job?.cancel()
        super.onCleared()
    }

}