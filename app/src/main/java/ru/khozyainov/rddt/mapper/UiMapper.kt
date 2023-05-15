package ru.khozyainov.rddt.mapper

import ru.khozyainov.data.models.ModelEntity
import ru.khozyainov.domain.model.Model

interface UiMapper<M : Model, ME : ModelEntity> {
    fun mapToDomain(entity: ME): M
    fun mapToEntity(model: M): ME
}