package ru.khozyainov.data.local.db

import android.content.Context
import androidx.room.Room
import ru.khozyainov.data.local.db.AppDatabase.Companion.DB_NAME

class RddtDatabase(
    context: Context
) {
    private val db = Room.databaseBuilder(
        context = context,
        klass = AppDatabase::class.java,
        name = DB_NAME
    ).build()

    val postDao = db.postDao()
    val remoteKeysDao = db.remoteKeysDao()
}