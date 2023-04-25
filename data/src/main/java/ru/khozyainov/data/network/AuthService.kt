package ru.khozyainov.data.network

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import net.openid.appauth.AuthorizationRequest
import net.openid.appauth.AuthorizationService
import net.openid.appauth.AuthorizationServiceConfiguration
import net.openid.appauth.ResponseTypeValues
import ru.khozyainov.data.BuildConfig

class AuthService(
    context: Context
) {

    val authorizationServiceConfiguration = AuthorizationServiceConfiguration(
        Uri.parse(OAUTH_URL),
        Uri.parse(TOKEN_URL),
        null,
        Uri.parse(TOKEN_REVOKE_URL)
    )

    val authorizationRequestBuilder = AuthorizationRequest.Builder(
        authorizationServiceConfiguration,
        CLIENT_ID,
        RESPONSE_TYPE,
        REDIRECT_URI.toUri()
    ).setScope(OAUTH_SCOPE)

    val authorizationService = AuthorizationService(context)

//    fun getAuthorizationRequestBuilder(
//        serviceConfiguration: AuthorizationServiceConfiguration
//    ): AuthorizationRequest.Builder = AuthorizationRequest.Builder(
//        serviceConfiguration,
//        CLIENT_ID,
//        RESPONSE_TYPE,
//        REDIRECT_URI.toUri()
//    ).setScope(OAUTH_SCOPE)
//    fun providesServiceConfiguration(): AuthorizationServiceConfiguration =
//        AuthorizationServiceConfiguration(
//            Uri.parse(OAUTH_URL),
//            Uri.parse(TOKEN_URL),
//            null,
//            Uri.parse(TOKEN_REVOKE_URL)
//        )

    companion object {
        const val OAUTH_URL = "https://www.reddit.com/api/v1/authorize"
        const val TOKEN_URL = "https://www.reddit.com/api/v1/access_token"
        const val TOKEN_REVOKE_URL = "https://www.reddit.com/api/v1/revoke_token"
        const val INSTALLED_CLIENT = "https://oauth.reddit.com/grants/installed_client"
        const val REDIRECT_URI = "ru.skillbox.humblr://reddit.com/callback"
        const val RESPONSE_TYPE = ResponseTypeValues.CODE
        const val CLIENT_ID = BuildConfig.CLIENT_ID
        const val OAUTH_SCOPE =
            "identity edit flair history modconfig modflair modlog modposts modwiki mysubreddits privatemessages read report save submit subscribe vote wikiedit wikiread"
        const val PARAMETER_DEVICE_ID = "device_id"
        const val AUTHORIZATION_HEADER = "Authorization"

        const val LOGOUT_REDIRECT_URI = "ru.skillbox.humblr://reddit.com/logout_callback"


    }
}