package ru.khozyainov.rddt.ui.login

import android.content.Intent
import app.cash.turbine.test
import kotlinx.coroutines.runBlocking
import net.openid.appauth.TokenRequest
import org.junit.After
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import ru.khozyainov.domain.usecase.login.GetLoginIntentUseCase
import ru.khozyainov.domain.usecase.login.GetAndSaveTokenByRequestUseCase
import ru.khozyainov.rddt.MainDispatcherRule

class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val getLoginIntentUseCase = mock<GetLoginIntentUseCase>()
    private val getAndSaveTokenByRequestUseCase = mock<GetAndSaveTokenByRequestUseCase>()

    @After
    fun tearDown() {
        reset(getLoginIntentUseCase, getAndSaveTokenByRequestUseCase)
    }

    @Test
    fun `refreshState should set login state Default`() = runBlocking {
        //given
        val viewModel = LoginViewModel(
            getLoginIntentUseCase = getLoginIntentUseCase,
            getAndSaveTokenByRequestUseCase = getAndSaveTokenByRequestUseCase
        )
        val expected = LoginState.Default

        //when
        viewModel.refreshState()
        viewModel.uiState.test {
            val actual = awaitItem()

            //then
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `setLoadingState should set login state Loading`() = runBlocking {
        //given
        val viewModel = LoginViewModel(
            getLoginIntentUseCase = getLoginIntentUseCase,
            getAndSaveTokenByRequestUseCase = getAndSaveTokenByRequestUseCase
        )
        val expected = LoginState.Loading

        //when
        viewModel.setLoadingState()
        viewModel.uiState.test {
            val actual = awaitItem()

            //then
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `onAuthFailed should set login state Error`() = runBlocking {
        //given
        val viewModel = LoginViewModel(
            getLoginIntentUseCase = getLoginIntentUseCase,
            getAndSaveTokenByRequestUseCase = getAndSaveTokenByRequestUseCase
        )
        val exception = Exception("some exception")
        val expected = LoginState.Error(exception = exception)

        //when
        viewModel.onAuthFailed(exception = exception)
        viewModel.uiState.test {
            val actual = awaitItem()

            //then
            Assert.assertEquals(expected, actual)
        }
    }

    @Test
    fun `getLoginPageIntent should set login state Success with intent`(): Unit = runBlocking {
        //given
        val intent = Intent("some action")
        val expected = LoginState.Success(intent = intent)
        val viewModel = LoginViewModel(
            getLoginIntentUseCase = getLoginIntentUseCase,
            getAndSaveTokenByRequestUseCase = getAndSaveTokenByRequestUseCase
        )

        //when
        whenever(getLoginIntentUseCase()).thenReturn(intent)
        viewModel.getLoginPageIntent()
        viewModel.uiState.test {
            val actual = awaitItem()

            //then
            Assert.assertEquals(expected, actual)
        }
        verify(getLoginIntentUseCase, times(1)).invoke()
    }


    @Test
    fun `getTokenByRequest should set login state NavigateToLaunchAction if token received`(): Unit = runBlocking {
        //given
        val tokenRequest = mock<TokenRequest>()
        val expected = LoginState.NavigateToLaunchAction
        val viewModel = LoginViewModel(
            getLoginIntentUseCase = getLoginIntentUseCase,
            getAndSaveTokenByRequestUseCase = getAndSaveTokenByRequestUseCase
        )

        //when
        whenever(getAndSaveTokenByRequestUseCase(tokenRequest = tokenRequest)).thenReturn(true)
        viewModel.getAndSaveTokenByRequest(tokenRequest = tokenRequest)
        viewModel.uiState.test {
            val actual = awaitItem()

            //then
            Assert.assertEquals(expected, actual)
        }
        verify(getAndSaveTokenByRequestUseCase, times(1)).invoke(tokenRequest = tokenRequest)
    }

    @Test
    fun `getTokenByRequest should set correct login state if token is not received`(): Unit = runBlocking {
        //given
        val tokenRequest = mock<TokenRequest>()
        val expected = LoginState.Loading
        val viewModel = LoginViewModel(
            getLoginIntentUseCase = getLoginIntentUseCase,
            getAndSaveTokenByRequestUseCase = getAndSaveTokenByRequestUseCase
        )

        //when
        viewModel.setLoadingState()
        whenever(getAndSaveTokenByRequestUseCase(tokenRequest = tokenRequest)).thenReturn(false)
        viewModel.getAndSaveTokenByRequest(tokenRequest = tokenRequest)
        viewModel.uiState.test {
            val actual = awaitItem()

            //then
            Assert.assertEquals(expected, actual)
        }
        verify(getAndSaveTokenByRequestUseCase, times(1)).invoke(tokenRequest = tokenRequest)
    }
}