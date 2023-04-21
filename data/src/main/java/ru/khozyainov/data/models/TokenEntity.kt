package ru.khozyainov.data.models

import ru.khozyainov.data.base.ModelEntity

data class TokenEntity(
    val accessToken: String
): ModelEntity()
