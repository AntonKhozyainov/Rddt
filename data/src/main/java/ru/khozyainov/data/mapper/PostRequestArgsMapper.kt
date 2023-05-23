package ru.khozyainov.data.mapper

import ru.khozyainov.data.models.PostRequestArgsData
import ru.khozyainov.domain.model.PostRequestArgs

class PostRequestArgsMapper(
   private val postSortTypeMapper : PostSortTypeMapper
): EntityMapper<PostRequestArgs, PostRequestArgsData> {

    override fun mapToDomain(entity: PostRequestArgsData): PostRequestArgs {
        return PostRequestArgs(
            searchString = entity.searchString,
            sort = postSortTypeMapper.mapToDomain(entity = entity.sort)
        )
    }

    override fun mapToEntity(model: PostRequestArgs): PostRequestArgsData {
        return PostRequestArgsData(
            searchString = model.searchString,
            sort = postSortTypeMapper.mapToEntity(model = model.sort)
        )
    }
}