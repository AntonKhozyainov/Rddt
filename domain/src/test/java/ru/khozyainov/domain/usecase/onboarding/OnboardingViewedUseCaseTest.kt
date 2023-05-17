package ru.khozyainov.domain.usecase.onboarding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.khozyainov.domain.repo.OnboardingRepository

class OnboardingViewedUseCaseTest {

    private val onboardingRepository = mock<OnboardingRepository>()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `should run without exception`() = runTest {
        whenever(onboardingRepository.setOnboardingState(onboarding = any()))
        val useCase = OnboardingViewedUseCase(onboardingRepository = onboardingRepository)
        useCase(onboarding = any())
    }
}