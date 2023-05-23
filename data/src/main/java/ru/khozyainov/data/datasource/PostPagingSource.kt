package ru.khozyainov.data.datasource

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.khozyainov.data.local.db.model.PostEntity
import ru.khozyainov.data.models.PostRequestArgsData

interface PostPagingSource {
    fun getPostEntities(requestArgs: PostRequestArgsData): Flow<PagingData<PostEntity>>
}