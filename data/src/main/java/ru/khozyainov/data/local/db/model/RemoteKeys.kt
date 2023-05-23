package ru.khozyainov.data.local.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.khozyainov.data.local.db.model.RemoteKeysContract.Columns.ID
import ru.khozyainov.data.local.db.model.RemoteKeysContract.Columns.NEXT
import ru.khozyainov.data.local.db.model.RemoteKeysContract.Columns.PREV
import ru.khozyainov.data.local.db.model.RemoteKeysContract.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class RemoteKeys(
    @PrimaryKey
    @ColumnInfo(name = ID) val id: String,
    @ColumnInfo(name = PREV) val prevKey: String?,
    @ColumnInfo(name = NEXT) val nextKey: String?
)
