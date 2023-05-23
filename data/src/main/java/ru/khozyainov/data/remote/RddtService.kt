package ru.khozyainov.data.remote

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import ru.khozyainov.data.remote.api.PostApi
import ru.khozyainov.data.remote.interceptor.AuthorizationFailedInterceptor
import ru.khozyainov.data.remote.interceptor.AuthorizationInterceptor

class RddtService(
    authorizationInterceptor: AuthorizationInterceptor,
    authorizationFailedInterceptor: AuthorizationFailedInterceptor
) {

    private val client = OkHttpClient.Builder()
        //TAG: okhttp.OkHttpClient
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addNetworkInterceptor(authorizationInterceptor)
        .addInterceptor(authorizationFailedInterceptor)
        .followRedirects(true)
        .build()

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .build()
                )
            )
            .client(client)
            .build()


    val postApi: PostApi
        get() = retrofit.create()

    companion object {
        private const val BASE_URL = "https://oauth.reddit.com/api/v1/"
    }
}