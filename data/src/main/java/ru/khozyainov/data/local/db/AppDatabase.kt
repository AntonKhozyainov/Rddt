package ru.khozyainov.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.khozyainov.data.local.db.AppDatabase.Companion.DB_VERSION
import ru.khozyainov.data.local.db.dao.PostDao
import ru.khozyainov.data.local.db.dao.RemoteKeysDao
import ru.khozyainov.data.local.db.model.PostEntity
import ru.khozyainov.data.local.db.model.RemoteKeys

@Database(
    entities = [
        PostEntity::class,
        RemoteKeys::class
    ],
    version = DB_VERSION
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "rddt_database"
    }
}