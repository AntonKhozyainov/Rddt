package ru.khozyainov.data.repo

import android.content.Intent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import net.openid.appauth.TokenRequest
import ru.khozyainov.data.datasource.AuthLocalDataSource
import ru.khozyainov.data.datasource.AuthRemoteDataSource
import ru.khozyainov.data.mapper.TokenMapper
import ru.khozyainov.domain.model.Token
import ru.khozyainov.domain.repo.AuthRepository

class AuthRepositoryImpl(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val tokenMapper: TokenMapper
) : AuthRepository {

    override fun getToken(): Flow<Token> =
        authLocalDataSource.getToken().map { tokenEntity ->
            tokenMapper.mapToDomain(tokenEntity)
        }

    override suspend fun getLoginPageIntent(): Intent {
        val deviceId = authLocalDataSource.getDeviceIDFlow().first()
        return authRemoteDataSource.getLoginPageIntent(deviceId)
    }

    override suspend fun getTokenByRequest(tokenRequest: TokenRequest): Boolean {
        val token = authRemoteDataSource.performTokenRequestSuspend(tokenRequest)
        return authLocalDataSource.setToken(token)
    }

}