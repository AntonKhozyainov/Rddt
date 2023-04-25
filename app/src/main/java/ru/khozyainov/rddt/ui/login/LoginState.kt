package ru.khozyainov.rddt.ui.login

import android.content.Intent

sealed class LoginState {
    data class Success(
        val intent: Intent? = null
    ) : LoginState()
    object NavigateToLaunchAction : LoginState()
    object Default : LoginState()
    object Loading : LoginState()
    data class Error(val exception: Throwable) : LoginState()
}
