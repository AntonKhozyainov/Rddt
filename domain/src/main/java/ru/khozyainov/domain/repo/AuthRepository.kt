package ru.khozyainov.domain.repo

import android.content.Intent
import kotlinx.coroutines.flow.Flow
import net.openid.appauth.TokenRequest
import ru.khozyainov.domain.model.Token

interface AuthRepository {
    fun getToken(): Flow<Token>
    suspend fun getLoginPageIntent(): Intent
    suspend fun getTokenByRequest(tokenRequest: TokenRequest): Boolean
}