package ru.khozyainov.data.remote.interceptor

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import net.openid.appauth.ClientSecretBasic
import net.openid.appauth.TokenRequest
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.closeQuietly
import ru.khozyainov.data.datasource.AuthLocalDataSource
import ru.khozyainov.data.models.TokenEntity
import ru.khozyainov.data.remote.AuthService
import ru.khozyainov.data.remote.AuthService.Companion.AUTHORIZATION_HEADER
import ru.khozyainov.data.remote.AuthService.Companion.CLIENT_ID
import ru.khozyainov.data.remote.AuthService.Companion.INSTALLED_CLIENT
import ru.khozyainov.data.remote.AuthService.Companion.OAUTH_SCOPE
import ru.khozyainov.data.remote.AuthService.Companion.PARAMETER_DEVICE_ID
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.coroutines.suspendCoroutine

class AuthorizationFailedInterceptor(
    private val authLocalDataSource: AuthLocalDataSource,
    private val authService: AuthService
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequestTimestamp = System.currentTimeMillis()
        val originalResponse = chain.proceed(chain.request())
        return originalResponse
            .takeIf { it.code != 401 }
        //если код ответа 401, обновляем токен
            ?: handleUnauthorizedResponse(chain, originalResponse, originalRequestTimestamp)
    }

    private fun handleUnauthorizedResponse(
        chain: Interceptor.Chain,
        originalResponse: Response,
        requestTimestamp: Long
    ): Response {
        val latch = getLatch()
        return when {
            latch != null && latch.count > 0 -> handleTokenIsUpdating(
                chain,
                latch,
                requestTimestamp,
                originalResponse
            )
                ?: originalResponse

            tokenUpdateTime > requestTimestamp -> updateTokenAndProceedChain(
                chain,
                originalResponse
            )

            else -> handleTokenNeedRefresh(chain, originalResponse) ?: originalResponse
        }
    }

    private fun handleTokenIsUpdating(
        chain: Interceptor.Chain,
        latch: CountDownLatch,
        requestTimestamp: Long,
        originalResponse: Response
    ): Response? {
        return if (latch.await(REQUEST_TIMEOUT, TimeUnit.SECONDS)
            && tokenUpdateTime > requestTimestamp
        ) {
            updateTokenAndProceedChain(chain, originalResponse)
        } else {
            null
        }
    }

    private fun handleTokenNeedRefresh(
        chain: Interceptor.Chain,
        originalResponse: Response
    ): Response? {
        return if (refreshToken()) {
            updateTokenAndProceedChain(chain, originalResponse)
        } else {
            null
        }
    }

    private fun updateTokenAndProceedChain(
        chain: Interceptor.Chain,
        originalResponse: Response
    ): Response {
        val newRequest = updateOriginalCallWithNewToken(chain.request())
        originalResponse.closeQuietly()
        return chain.proceed(newRequest)
    }

    private fun refreshToken(): Boolean {
        initLatch()

        val tokenRefreshed = runBlocking {
            runCatching {
                refreshTokenService()
            }.getOrNull()
                ?.let { token ->
                    authLocalDataSource.setToken(
                        TokenEntity(accessToken = token)
                    )
                } ?: false
        }

        if (tokenRefreshed) {
            tokenUpdateTime = System.currentTimeMillis()
        } else {
            // не удалось обновить токен, произвести логаут
//            unauthorizedHandler.onUnauthorized()
            //Timber.d("logout after token refresh failure")
        }
        getLatch()?.countDown()
        return tokenRefreshed
    }

    private suspend fun refreshTokenService(): String {

        val id = authLocalDataSource.getDeviceIDFlow().first()

        val tokenRequest = TokenRequest.Builder(
            authService.authorizationServiceConfiguration,
            CLIENT_ID
        )
            .setAdditionalParameters(
                mapOf((PARAMETER_DEVICE_ID to id))
            )
            .setGrantType(INSTALLED_CLIENT)
            .setScopes(OAUTH_SCOPE)
            .build()

        return performTokenRequestSuspend(tokenRequest)
    }

    private suspend fun performTokenRequestSuspend(
        tokenRequest: TokenRequest
    ): String = suspendCoroutine { continuation ->
        authService.authorizationService.performTokenRequest(
            tokenRequest,
            //Если сервис не требует client_secret, то можно использовать ClientSecretBasic("")
            ClientSecretBasic("")
        ) { response, exception ->
            when {
                response != null -> {
                    //получение токена произошло успешно
                    continuation.resumeWith(Result.success(response.accessToken.orEmpty()))
                }
                //получение токенов произошло неуспешно, показываем ошибку
                exception != null -> {
                    continuation.resumeWith(Result.failure(exception))
                }

                else -> error("unreachable")
            }
        }

    }

    private fun updateOriginalCallWithNewToken(request: Request): Request {
        return runBlocking {
            request
                .newBuilder()
                .header(
                    AUTHORIZATION_HEADER,
                    "Bearer ${authLocalDataSource.getToken().first().accessToken}"
                )
                .build()
        }
    }

    companion object {

        private const val REQUEST_TIMEOUT = 30L

        @Volatile
        private var tokenUpdateTime: Long = 0L

        private var countDownLatch: CountDownLatch? = null

        @Synchronized
        fun initLatch() {
            countDownLatch = CountDownLatch(1)
        }

        @Synchronized
        fun getLatch() = countDownLatch
    }
}
