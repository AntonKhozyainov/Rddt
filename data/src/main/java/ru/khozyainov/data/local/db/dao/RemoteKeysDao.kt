package ru.khozyainov.data.local.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import ru.khozyainov.data.local.db.model.RemoteKeys
import ru.khozyainov.data.local.db.model.RemoteKeysContract.Columns.ID
import ru.khozyainov.data.local.db.model.RemoteKeysContract.TABLE_NAME

@Dao
interface RemoteKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(remoteKeys: List<RemoteKeys>)

    @Query("SELECT * FROM $TABLE_NAME WHERE $ID = :postId")
    suspend fun selectByPostId(postId: String): RemoteKeys?

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun clear()

    @Transaction
    suspend fun refresh(remoteKeys: List<RemoteKeys>) {
        clear()
        insert(remoteKeys)
    }
}