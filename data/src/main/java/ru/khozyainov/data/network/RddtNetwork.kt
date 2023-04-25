package ru.khozyainov.data.network

import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class RddtNetwork(
    private val authorizationInterceptor: AuthorizationInterceptor,
    //private val authorizationFailedInterceptor: AuthorizationFailedInterceptor
) {

    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder()
        //TAG: okhttp.OkHttpClient
        .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addNetworkInterceptor(authorizationInterceptor)
        //.addInterceptor(authorizationFailedInterceptor)
        .followRedirects(true)
        .build()

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                MoshiConverterFactory.create(
                    Moshi.Builder()
                        .build()
                )
            )
            .client(
                providesOkHttpClient()
            )
            .build()
    }


    companion object {
        private const val BASE_URL = "https://oauth.reddit.com/api/v1/"
    }
}