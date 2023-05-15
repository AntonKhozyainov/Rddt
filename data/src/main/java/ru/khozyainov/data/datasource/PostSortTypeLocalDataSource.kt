package ru.khozyainov.data.datasource

import kotlinx.coroutines.flow.Flow
import ru.khozyainov.data.models.PostSortTypeEntity

interface PostSortTypeLocalDataSource {
    fun getSortType(): Flow<PostSortTypeEntity>
    suspend fun saveSortType(sortType: PostSortTypeEntity)
}