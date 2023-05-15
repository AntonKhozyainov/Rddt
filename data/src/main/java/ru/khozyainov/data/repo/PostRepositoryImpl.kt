package ru.khozyainov.data.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.khozyainov.data.datasource.PostSortTypeLocalDataSource
import ru.khozyainov.data.mapper.PostSortTypeMapper
import ru.khozyainov.domain.model.PostSortType
import ru.khozyainov.domain.repo.PostRepository

class PostRepositoryImpl(
    private val postSortTypeLocalDataSource: PostSortTypeLocalDataSource,
    private val postSortTypeMapper: PostSortTypeMapper
): PostRepository {

    override fun getSortType(): Flow<PostSortType> {
        return postSortTypeLocalDataSource.getSortType().map { postSortTypeEntity ->
            postSortTypeMapper.mapToDomain(entity = postSortTypeEntity)
        }
    }

    override suspend fun setSortType(sortType: PostSortType) {
        postSortTypeLocalDataSource.saveSortType(
            sortType = postSortTypeMapper.mapToEntity(model = sortType)
        )
    }
}