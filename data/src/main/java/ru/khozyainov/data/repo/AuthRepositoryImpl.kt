package ru.khozyainov.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.khozyainov.data.datasource.AuthLocalDataSource
import ru.khozyainov.data.mapper.TokenMapper
import ru.khozyainov.domain.model.Token
import ru.khozyainov.domain.repo.AuthRepository

class AuthRepositoryImpl(
    private val authLocalDataSource: AuthLocalDataSource,
    private val tokenMapper: TokenMapper
) : AuthRepository {

    override fun getToken(): Flow<Token> =
        authLocalDataSource.getToken().map { tokenEntity ->
            tokenMapper.mapToDomain(tokenEntity)
        }

}