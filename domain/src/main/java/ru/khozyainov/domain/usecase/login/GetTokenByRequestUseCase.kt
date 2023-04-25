package ru.khozyainov.domain.usecase.login

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import net.openid.appauth.TokenRequest
import ru.khozyainov.domain.repo.AuthRepository

class GetTokenByRequestUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(tokenRequest: TokenRequest): Boolean = execute(tokenRequest)
    private suspend fun execute(tokenRequest: TokenRequest): Boolean = withContext(Dispatchers.IO) {
        authRepository.getTokenByRequest(tokenRequest)
    }
}