package ru.khozyainov.rddt.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ru.khozyainov.domain.model.Onboarding
import ru.khozyainov.domain.usecase.onboarding.OnboardingViewedUseCase

class OnboardingViewModel(
    private val onboardingViewedUseCase: OnboardingViewedUseCase
): ViewModel() {

    private val iuMutableState = MutableStateFlow<OnboardingState>(OnboardingState.Default)
    val uiState: StateFlow<OnboardingState> = iuMutableState

    private val errorHandler = CoroutineExceptionHandler{ _, throwable ->
        iuMutableState.value = OnboardingState.Error(exception = throwable)
    }

    fun onboardingCompleted() = viewModelScope.launch(errorHandler) {
        onboardingViewedUseCase(onboarding = Onboarding(viewed = true))
        iuMutableState.value = OnboardingState.Success
    }
}