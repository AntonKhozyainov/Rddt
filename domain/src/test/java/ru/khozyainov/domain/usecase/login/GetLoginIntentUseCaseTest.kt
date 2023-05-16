package ru.khozyainov.domain.usecase.login

import android.content.Intent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.khozyainov.domain.repo.AuthRepository

@OptIn(ExperimentalCoroutinesApi::class)
class GetLoginIntentUseCaseTest {

    private val authRepository = mock<AuthRepository>()

    @Test
    fun shouldReturnCurrentIntent() = runTest {
        //given
        val expected = Intent("Some action")

        //when
        whenever(authRepository.getLoginPageIntent()).thenReturn(expected)
        val actual = GetLoginIntentUseCase(authRepository = authRepository).invoke()

        //then
        assertEquals(expected, actual)
    }
}