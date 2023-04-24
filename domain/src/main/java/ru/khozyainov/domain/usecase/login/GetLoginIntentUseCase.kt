package ru.khozyainov.domain.usecase.login

import ru.khozyainov.domain.repo.AuthRepository

class GetLoginIntentUseCase(
    private val authRepository: AuthRepository
) {
    //operator fun invoke(): Flow<Token> = authRepository.getToken()


}
