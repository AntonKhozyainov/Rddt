package ru.khozyainov.rddt.ui.onboarding

import app.cash.turbine.test
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.khozyainov.domain.model.Onboarding
import ru.khozyainov.domain.usecase.onboarding.OnboardingViewedUseCase
import ru.khozyainov.rddt.MainDispatcherRule

class OnboardingViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val onboardingViewedUseCase = mock<OnboardingViewedUseCase>()

    @After
    fun tearDown(){
        reset(onboardingViewedUseCase)
    }

    @Test
    fun `onboardingCompleted should return onboarding state success`() = runBlocking {
        //given
        val onboardingTest = Onboarding(viewed = true)
        val expected = OnboardingState.Success
        val viewModel = OnboardingViewModel(onboardingViewedUseCase = onboardingViewedUseCase)

        //when
        whenever(onboardingViewedUseCase.invoke(onboardingTest)).thenReturn(Unit)
        viewModel.onboardingCompleted()

        viewModel.uiState.test {
            val actual = awaitItem()

            //then
            Assert.assertEquals(expected, actual)
            verify(onboardingViewedUseCase, times(1)).invoke(onboarding = onboardingTest)
        }
    }

    @Test
    fun `refreshState should return onboarding state default`() = runBlocking {
        //given
        val viewModel = OnboardingViewModel(onboardingViewedUseCase = onboardingViewedUseCase)
        val expected = OnboardingState.Default

        //when
        viewModel.refreshState()

        viewModel.uiState.test {
            val actual = awaitItem()
            //then
            Assert.assertEquals(expected, actual)
        }
    }
}