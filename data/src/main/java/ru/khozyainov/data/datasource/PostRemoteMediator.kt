package ru.khozyainov.data.datasource

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import retrofit2.HttpException
import ru.khozyainov.data.local.db.RddtDatabase
import ru.khozyainov.data.local.db.model.PostEntity
import ru.khozyainov.data.local.db.model.RemoteKeys
import ru.khozyainov.data.models.PostRequestArgsData
import ru.khozyainov.data.remote.RddtService
import java.io.IOException

@OptIn(ExperimentalPagingApi::class)
class PostRemoteMediator(
    private val requestArgs: PostRequestArgsData,
    private val service: RddtService,
    private val database: RddtDatabase
) : RemoteMediator<Int, PostEntity>() {

    override suspend fun initialize(): InitializeAction {
        //return super.initialize()
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, PostEntity>
    ): MediatorResult {

        val nextPrevKeys: Pair<String?, String?> = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey to null
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                val prevKey = remoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                null to prevKey
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                val nextKey = remoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                nextKey to null
            }
        }

        try {

            val postEntityList = getPostEntityList(nextPrevKeys, state.config.pageSize)
            val endOfPaginationReached = postEntityList.isEmpty()


            val prevKey = nextPrevKeys.second
            val nextKey = nextPrevKeys.first
            val keys = postEntityList.map {
                RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
            }

            if (loadType == LoadType.REFRESH) {
                database.postDao.refresh(postList = postEntityList)
                database.remoteKeysDao.refresh(remoteKeys = keys)
            } else{
                database.postDao.insert(postList = postEntityList)
                database.remoteKeysDao.insert(remoteKeys = keys)
            }

            return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)

        } catch (exception: IOException) {
            return MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            return MediatorResult.Error(exception)
        }

    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, PostEntity>
    ): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { postId ->
                database.remoteKeysDao.selectByPostId(postId = postId)
            }
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, PostEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { postEntity ->
                database.remoteKeysDao.selectByPostId(postId = postEntity.id)
            }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, PostEntity>): RemoteKeys? {
        return state.pages.lastOrNull{ it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { postEntity ->
                database.remoteKeysDao.selectByPostId(postId = postEntity.id)
            }
    }

    private suspend fun getPostEntityList(
        nextPrevKeys: Pair<String?, String?>,
        limit: Int
    ): List<PostEntity> =
        if (requestArgs.searchString.isBlank()) {
            service.postApi.getPosts(
                sort = requestArgs.sort.sortType,
                limit = limit,
                after = nextPrevKeys.first,
                before = nextPrevKeys.second
            )
        } else {
            service.postApi.searchPosts(
                query = requestArgs.searchString,
                sort = requestArgs.sort.sortType,
                limit = limit,
                after = nextPrevKeys.first,
                before = nextPrevKeys.second
            )

        }
}