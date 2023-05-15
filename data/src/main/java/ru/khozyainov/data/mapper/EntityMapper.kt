package ru.khozyainov.data.mapper

import ru.khozyainov.data.models.ModelEntity
import ru.khozyainov.domain.model.Model

interface EntityMapper<M : Model, ME : ModelEntity> {
    fun mapToDomain(entity: ME): M
    fun mapToEntity(model: M): ME
}