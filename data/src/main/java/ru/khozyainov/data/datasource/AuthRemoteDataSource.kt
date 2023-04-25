package ru.khozyainov.data.datasource

import android.content.Intent
import net.openid.appauth.TokenRequest
import ru.khozyainov.data.models.TokenEntity

interface AuthRemoteDataSource {

    fun getLoginPageIntent(deviceId: String): Intent

    suspend fun performTokenRequestSuspend(tokenRequest: TokenRequest): TokenEntity
}