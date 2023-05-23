package ru.khozyainov.data.remote.interceptor

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import ru.khozyainov.data.datasource.AuthLocalDataSource
import ru.khozyainov.data.remote.AuthService.Companion.AUTHORIZATION_HEADER

class AuthorizationInterceptor(
    private val authLocalDataSource: AuthLocalDataSource
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val modifiedRequest = originalRequest.newBuilder()
            .apply {
                val token = runBlocking {
                    authLocalDataSource.getToken().first()
                }
                header(AUTHORIZATION_HEADER, "Bearer ${token.accessToken}")
            }
            .build()

        return chain.proceed(modifiedRequest)
    }
}
