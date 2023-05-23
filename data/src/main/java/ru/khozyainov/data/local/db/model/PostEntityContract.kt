package ru.khozyainov.data.local.db.model

object PostEntityContract {

    const val TABLE_NAME = "post"

    object Columns{
        const val ID = "id"
        const val SEARCH = "search_for_rm"
        const val CREATED_AT = "created_at"
    }
}