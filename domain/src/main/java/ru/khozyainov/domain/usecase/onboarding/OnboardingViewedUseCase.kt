package ru.khozyainov.domain.usecase.onboarding

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.khozyainov.domain.model.Onboarding
import ru.khozyainov.domain.repo.OnboardingRepository

class OnboardingViewedUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    suspend operator fun invoke(onboarding: Onboarding) = execute(onboarding)
    private suspend fun execute(onboarding: Onboarding) = withContext(Dispatchers.IO){
        onboardingRepository.setOnboardingState(onboarding)
    }
}