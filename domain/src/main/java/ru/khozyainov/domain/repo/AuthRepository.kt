package ru.khozyainov.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.khozyainov.domain.model.Token

interface AuthRepository {
    fun getToken(): Flow<Token>
}