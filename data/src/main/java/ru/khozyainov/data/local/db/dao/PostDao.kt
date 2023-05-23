package ru.khozyainov.data.local.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.khozyainov.data.local.db.model.PostEntity
import ru.khozyainov.data.local.db.model.PostEntityContract.Columns.CREATED_AT
import ru.khozyainov.data.local.db.model.PostEntityContract.Columns.SEARCH
import ru.khozyainov.data.local.db.model.PostEntityContract.TABLE_NAME

@Dao
interface PostDao {

    @Query("SELECT * FROM $TABLE_NAME " +
            "WHERE (:query IS NULL OR $SEARCH = :query)  " +
            "ORDER BY $CREATED_AT")
    fun select(
        query: String
    ): PagingSource<Int, PostEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(postList: List<PostEntity>)

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun clear()

    @Transaction
    suspend fun refresh(postList: List<PostEntity>) {
        clear()
        insert(postList)
    }
}