package ru.khozyainov.data.datasource

import android.content.Intent
import androidx.browser.customtabs.CustomTabsIntent
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.TokenRequest
import ru.khozyainov.data.models.TokenEntity
import ru.khozyainov.data.remote.AuthService
import ru.khozyainov.data.remote.AuthService.Companion.PARAMETER_DEVICE_ID
import kotlin.coroutines.suspendCoroutine

class AuthRemoteDataSourceImpl(
    private val authService: AuthService
) : AuthRemoteDataSource {

    override fun getLoginPageIntent(deviceId: String): Intent {
        return authService.authorizationService.getAuthorizationRequestIntent(
            authService.authorizationRequestBuilder.setAdditionalParameters(
                mapOf(PARAMETER_DEVICE_ID to deviceId)
            ).build(),
            CustomTabsIntent.Builder().build()
        )
    }

    override suspend fun performTokenRequestSuspend(tokenRequest: TokenRequest): TokenEntity {
        return suspendCoroutine { continuation ->
            authService.authorizationService.performTokenRequest(
                tokenRequest,
                ClientSecretBasic("")
            ) { response, exception ->
                when {
                    response != null -> {
                        continuation.resumeWith(
                            Result.success(
                                TokenEntity(accessToken = response.accessToken.orEmpty())
                            )
                        )
                    }

                    exception != null -> {
                        continuation.resumeWith(Result.failure(exception))
                    }
                }
            }
        }
    }

}