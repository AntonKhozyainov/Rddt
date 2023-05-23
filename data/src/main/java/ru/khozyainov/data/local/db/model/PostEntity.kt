package ru.khozyainov.data.local.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.khozyainov.data.local.db.model.PostEntityContract.Columns.CREATED_AT
import ru.khozyainov.data.local.db.model.PostEntityContract.Columns.ID
import ru.khozyainov.data.local.db.model.PostEntityContract.Columns.SEARCH
import ru.khozyainov.data.local.db.model.PostEntityContract.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class PostEntity(
    @PrimaryKey
    @ColumnInfo(name = ID) val id: String,
    @ColumnInfo(name = SEARCH) val search: String,
    @ColumnInfo(name = CREATED_AT) val createdAt: Long
)
