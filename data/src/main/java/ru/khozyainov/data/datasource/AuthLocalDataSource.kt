package ru.khozyainov.data.datasource

import kotlinx.coroutines.flow.Flow
import ru.khozyainov.data.models.TokenEntity

interface AuthLocalDataSource {
    fun getToken(): Flow<TokenEntity>
    suspend fun setToken(tokenEntity: TokenEntity): Boolean
    fun getDeviceIDFlow(): Flow<String>
}