package ru.khozyainov.data.base

import ru.khozyainov.domain.model.Model

interface EntityMapper<M : Model, ME : ModelEntity> {
    fun mapToDomain(entity: ME): M
    fun mapToEntity(model: M): ME
}