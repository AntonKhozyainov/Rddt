package ru.khozyainov.data.mapper

import ru.khozyainov.data.models.PostSortTypeEntity
import ru.khozyainov.domain.model.PostSortType

class PostSortTypeMapper: EntityMapper<PostSortType, PostSortTypeEntity> {

    override fun mapToDomain(entity: PostSortTypeEntity): PostSortType {
        return PostSortType(sort = entity.sortType)
    }

    override fun mapToEntity(model: PostSortType): PostSortTypeEntity {
        return PostSortTypeEntity(sortType = model.sort)
    }
}