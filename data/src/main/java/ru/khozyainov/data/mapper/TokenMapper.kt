package ru.khozyainov.data.mapper

import ru.khozyainov.data.models.TokenEntity
import ru.khozyainov.domain.model.Token

class TokenMapper: EntityMapper<Token, TokenEntity> {

    override fun mapToDomain(entity: TokenEntity): Token = Token(
        accessToken = entity.accessToken
    )

    override fun mapToEntity(model: Token): TokenEntity = TokenEntity(
        accessToken = model.accessToken
    )
}