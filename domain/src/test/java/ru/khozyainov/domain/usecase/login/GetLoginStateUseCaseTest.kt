package ru.khozyainov.domain.usecase.login

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.khozyainov.domain.model.Token
import ru.khozyainov.domain.repo.AuthRepository

@OptIn(ExperimentalCoroutinesApi::class)
class GetLoginStateUseCaseTest {

    private val authRepository = mock<AuthRepository>()

    @Test
    fun shouldReturnCurrentToken() = runTest {
        //given
        val expected = Token(accessToken = "123")

        //when
        whenever(authRepository.getToken()).thenReturn(flow { emit(expected) })
        val actual = GetLoginStateUseCase(authRepository = authRepository).invoke().first()

        //then
        assertEquals(expected, actual)
    }
}