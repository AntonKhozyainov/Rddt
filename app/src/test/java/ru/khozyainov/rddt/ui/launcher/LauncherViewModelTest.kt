package ru.khozyainov.rddt.ui.launcher

import app.cash.turbine.test
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import ru.khozyainov.domain.model.Onboarding
import ru.khozyainov.domain.model.Token
import ru.khozyainov.domain.usecase.login.GetLoginStateUseCase
import ru.khozyainov.domain.usecase.onboarding.GetOnboardingStateUseCase
import ru.khozyainov.rddt.MainDispatcherRule
import java.lang.Exception

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Parameterized::class)
class LauncherViewModelTest(
    private val getOnboardingStateUseCaseReturn : Onboarding,
    private val getLoginStateUseCaseReturn : Token,
    private val expected: LauncherState
) {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getOnboardingStateUseCaseTest = mock<GetOnboardingStateUseCase>()
    private val getLoginStateUseCaseTest = mock<GetLoginStateUseCase>()

    @After
    fun tearDown(){
        reset(getOnboardingStateUseCaseTest, getLoginStateUseCaseTest)
    }

    @Test
    fun `after init view model should be set correct state`() = runBlocking{
        //when
        whenever(getOnboardingStateUseCaseTest.invoke()).thenReturn(flow{ emit(getOnboardingStateUseCaseReturn)})
        whenever(getLoginStateUseCaseTest.invoke()).thenReturn(flow{ emit(getLoginStateUseCaseReturn)})

        LauncherViewModel(
            getOnboardingStateUseCase = getOnboardingStateUseCaseTest,
            getLoginStateUseCase = getLoginStateUseCaseTest
        ).uiState.test {
            awaitItem() //init LauncherState.Default
            val actual = awaitItem()
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `when throw exception should be set error state`() = runBlocking{
        //given
        val exception = Exception("Exception")

        //when
        whenever(getOnboardingStateUseCaseTest.invoke()).thenReturn(flow{ emit(throw exception)})
        whenever(getLoginStateUseCaseTest.invoke()).thenReturn(flow{ emit(throw exception)})

        LauncherViewModel(
            getOnboardingStateUseCase = getOnboardingStateUseCaseTest,
            getLoginStateUseCase = getLoginStateUseCaseTest
        ).uiState.test {
            awaitItem() //init LauncherState.Default
            val actual = awaitItem()
            assert(actual is LauncherState.Error)
        }
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(Onboarding(viewed = false), Token(accessToken = ""), LauncherState.NavigateToOnboardingAction),
                arrayOf(Onboarding(viewed = true), Token(accessToken = ""), LauncherState.NavigateToLoginAction),
                arrayOf(Onboarding(viewed = true), Token(accessToken = "some token"), LauncherState.NavigateToMainActivityAction),
                arrayOf(Onboarding(viewed = false), Token(accessToken = "some token"), LauncherState.NavigateToOnboardingAction)
            )
        }
    }
}