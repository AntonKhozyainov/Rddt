package ru.khozyainov.domain.repo

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.khozyainov.domain.model.Post
import ru.khozyainov.domain.model.PostRequestArgs
import ru.khozyainov.domain.model.PostSortType

interface PostRepository {
    fun getSortType(): Flow<PostSortType>
    suspend fun setSortType(sortType: PostSortType)
    fun getPosts(args: PostRequestArgs): Flow<PagingData<Post>>
}