package ru.khozyainov.domain.usecase.onboarding

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import ru.khozyainov.domain.model.Onboarding
import ru.khozyainov.domain.repo.OnboardingRepository

class GetOnboardingStateUseCaseTest {

    private val onboardingRepository = mock<OnboardingRepository>()

    @After
    fun tearDown() {
        reset(onboardingRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun shouldReturnCorrectDataFromRepository() = runTest {

        //given
        val testOnboardingFlow = flow { emit(Onboarding(viewed = true)) }
        val expected = flow { emit(Onboarding(viewed = true)) }

        //when
        whenever(onboardingRepository.getOnboardingState()).thenReturn(testOnboardingFlow)
        val useCase = GetOnboardingStateUseCase(onboardingRepository = onboardingRepository)
        val actual = useCase()

        //then
        assertEquals(expected.first(), actual.first())
    }
}