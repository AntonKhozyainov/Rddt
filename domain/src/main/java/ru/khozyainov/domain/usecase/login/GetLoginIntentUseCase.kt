package ru.khozyainov.domain.usecase.login

import android.content.Intent
import ru.khozyainov.domain.repo.AuthRepository

class GetLoginIntentUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(): Intent = authRepository.getLoginPageIntent()

}
