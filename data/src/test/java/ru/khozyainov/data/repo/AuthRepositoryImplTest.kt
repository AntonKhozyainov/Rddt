package ru.khozyainov.data.repo

import android.content.Intent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import net.openid.appauth.TokenRequest
import org.junit.After
import org.junit.Assert
import org.junit.Test
import ru.khozyainov.data.datasource.AuthLocalDataSource
import ru.khozyainov.data.datasource.AuthRemoteDataSource
import ru.khozyainov.data.mapper.TokenMapper
import org.mockito.kotlin.mock
import org.mockito.kotlin.reset
import org.mockito.kotlin.whenever
import ru.khozyainov.data.models.TokenEntity
import ru.khozyainov.domain.model.Token

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    private val authLocalDataSource = mock<AuthLocalDataSource>()
    private val authRemoteDataSource = mock<AuthRemoteDataSource>()
    private val tokenMapper = TokenMapper()

    @After
    fun tearDown() {
        reset(authLocalDataSource, authRemoteDataSource)
    }

    @Test
    fun `getToken should return correct token`() = runTest{
        //given
        val accessToken = "some token"
        val expected = Token(accessToken = accessToken)
        val tokenEntity = TokenEntity(accessToken = accessToken)
        val repo = AuthRepositoryImpl(
            authLocalDataSource = authLocalDataSource,
            authRemoteDataSource = authRemoteDataSource,
            tokenMapper
        )

        //when
        whenever(authLocalDataSource.getToken()).thenReturn(flow { emit(tokenEntity) })
        val actual = repo.getToken().first()

        //then
        Assert.assertEquals(expected, actual)
    }


    @Test
    fun `getLoginPageIntent should return intent`() = runTest{
        //given
        val deviceId = "device id"
        val expected = Intent("some action")
        val repo = AuthRepositoryImpl(
            authLocalDataSource = authLocalDataSource,
            authRemoteDataSource = authRemoteDataSource,
            tokenMapper
        )

        //when
        whenever(authLocalDataSource.getDeviceIDFlow()).thenReturn(flow { emit(deviceId) })
        whenever(authRemoteDataSource.getLoginPageIntent(deviceId = deviceId)).thenReturn(expected)
        val actual = repo.getLoginPageIntent()

        //then
        Assert.assertEquals(expected, actual)
    }


    @Test
    fun `getAndSaveTokenByRequest should return true`() = runTest{
        //given
        val tokenRequest = mock<TokenRequest>()
        val expected = true
        val tokenEntity = TokenEntity(accessToken = "some token")
        val repo = AuthRepositoryImpl(
            authLocalDataSource = authLocalDataSource,
            authRemoteDataSource = authRemoteDataSource,
            tokenMapper
        )

        //when
        whenever(authRemoteDataSource.performTokenRequestSuspend(tokenRequest = tokenRequest)).thenReturn(tokenEntity)
        whenever(authLocalDataSource.setToken(tokenEntity)).thenReturn(expected)
        val actual = repo.getAndSaveTokenByRequest(tokenRequest = tokenRequest)

        //then
        Assert.assertEquals(expected, actual)
    }
}