package ru.khozyainov.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.khozyainov.data.local.db.RddtDatabase
import ru.khozyainov.data.local.db.model.PostEntity
import ru.khozyainov.data.models.PostRequestArgsData
import ru.khozyainov.data.remote.RddtService

@OptIn(ExperimentalPagingApi::class)
class PostPagingSourceImpl(
    private val service: RddtService,
    private val database: RddtDatabase
): PostPagingSource  {

    override fun getPostEntities(requestArgs: PostRequestArgsData): Flow<PagingData<PostEntity>> {

        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = PostRemoteMediator(
                requestArgs = requestArgs,
                service = service,
                database = database
            ),
            pagingSourceFactory = { database.postDao.select(query = requestArgs.searchString) }
        ).flow
    }

    companion object {
        const val NETWORK_PAGE_SIZE = 30
    }

}