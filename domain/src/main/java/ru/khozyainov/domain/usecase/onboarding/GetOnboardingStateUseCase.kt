package ru.khozyainov.domain.usecase.onboarding

import ru.khozyainov.domain.repo.OnboardingRepository

class GetOnboardingStateUseCase(
    private val onboardingRepository: OnboardingRepository
) {
    operator fun invoke() = onboardingRepository.getOnboardingState()
}