package ru.khozyainov.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.khozyainov.data.local.db.model.PostEntity

interface PostApi {

    @GET("/{sort}")
    suspend fun getPosts(
        @Path("sort") sort: String,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): List<PostEntity>

    @GET("/search")
    suspend fun searchPosts(
        @Query("q") query: String,
        @Query("sort") sort: String,
        @Query("limit") limit: Int,
        @Query("after") after: String? = null,
        @Query("before") before: String? = null
    ): List<PostEntity>
}