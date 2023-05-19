package ru.khozyainov.rddt.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import net.openid.appauth.TokenRequest
import ru.khozyainov.domain.usecase.login.GetLoginIntentUseCase
import ru.khozyainov.domain.usecase.login.GetAndSaveTokenByRequestUseCase

class LoginViewModel(
    private val getLoginIntentUseCase: GetLoginIntentUseCase,
    private val getAndSaveTokenByRequestUseCase: GetAndSaveTokenByRequestUseCase
) : ViewModel() {

    private val uiMutableState = MutableStateFlow<LoginState>(LoginState.Default)
    val uiState: StateFlow<LoginState> = uiMutableState

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        uiMutableState.value = LoginState.Error(exception = throwable)
    }

    fun refreshState() {
        uiMutableState.value = LoginState.Default
    }

    fun getLoginPageIntent() {
        setLoadingState()
        viewModelScope.launch(errorHandler) {
            val intent = getLoginIntentUseCase()
            uiMutableState.value = LoginState.Success(intent = intent)
        }
    }

    fun getAndSaveTokenByRequest(tokenRequest: TokenRequest) {
        setLoadingState()
        viewModelScope.launch(errorHandler) {
            val tokenReceived = getAndSaveTokenByRequestUseCase(tokenRequest)
            if (tokenReceived) {
                uiMutableState.value = LoginState.NavigateToLaunchAction
            }
        }
    }

    fun setLoadingState() {
        uiMutableState.value = LoginState.Loading
    }

    fun onAuthFailed(exception: Exception) {
        uiMutableState.value = LoginState.Error(exception = exception)
    }

}