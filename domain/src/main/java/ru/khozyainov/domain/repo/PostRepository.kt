package ru.khozyainov.domain.repo

import kotlinx.coroutines.flow.Flow
import ru.khozyainov.domain.model.PostSortType

interface PostRepository {
    fun getSortType(): Flow<PostSortType>
    suspend fun setSortType(sortType: PostSortType)
}