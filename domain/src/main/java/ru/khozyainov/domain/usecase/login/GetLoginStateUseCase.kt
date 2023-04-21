package ru.khozyainov.domain.usecase.login

import kotlinx.coroutines.flow.Flow
import ru.khozyainov.domain.model.Token
import ru.khozyainov.domain.repo.AuthRepository

class GetLoginStateUseCase(
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Token> = authRepository.getToken()
}