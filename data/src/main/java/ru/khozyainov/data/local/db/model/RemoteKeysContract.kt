package ru.khozyainov.data.local.db.model

object RemoteKeysContract {

    const val TABLE_NAME = "remote_keys"

    object Columns{
        const val ID = "id"
        const val PREV = "prev_key"
        const val NEXT = "next_key"
    }
}