package ru.khozyainov.data.repo

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.khozyainov.data.datasource.PostPagingSource
import ru.khozyainov.data.datasource.PostSortTypeLocalDataSource
import ru.khozyainov.data.mapper.PostRequestArgsMapper
import ru.khozyainov.data.mapper.PostSortTypeMapper
import ru.khozyainov.domain.model.Post
import ru.khozyainov.domain.model.PostRequestArgs
import ru.khozyainov.domain.model.PostSortType
import ru.khozyainov.domain.repo.PostRepository

class PostRepositoryImpl(
    private val postPagingSource: PostPagingSource,
    private val postSortTypeLocalDataSource: PostSortTypeLocalDataSource,
    private val postRequestArgsMapper: PostRequestArgsMapper,
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

    override fun getPosts(args: PostRequestArgs): Flow<PagingData<Post>> {
        return postPagingSource.getPostEntities(
            requestArgs = postRequestArgsMapper.mapToEntity(model = args)
        ).map { pagingData ->
            pagingData.map { postEntity ->
                Post(id = postEntity.id)//TODO add mapper
            }
        }
    }
}