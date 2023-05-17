package ru.khozyainov.domain.usecase.login

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import net.openid.appauth.TokenRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import ru.khozyainov.domain.repo.AuthRepository

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(Parameterized::class)
class GetTokenByRequestUseCaseTest(
    private val expected: Boolean
) {

    @After
    fun tearDown() {
        reset(authRepository)
    }

    @Test
    fun `should return correct answer`() = runTest {
        //given
        val testTokenRequest = mock<TokenRequest>()

        //when
        whenever(authRepository.getTokenByRequest(tokenRequest = testTokenRequest)).thenReturn(
            expected
        )
        val useCase = GetTokenByRequestUseCase(authRepository = authRepository)
        val actual = useCase(tokenRequest = testTokenRequest)

        //then
        assertEquals(expected, actual)
    }

    companion object {

        private val authRepository = mock<AuthRepository>()

        @JvmStatic
        @Parameterized.Parameters
        fun data(): Collection<Array<Any>> {
            return listOf(
                arrayOf(true), arrayOf(false)
            )
        }
    }
}