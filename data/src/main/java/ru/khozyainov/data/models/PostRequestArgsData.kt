package ru.khozyainov.data.models

data class PostRequestArgsData(
    val searchString: String,
    val sort: PostSortTypeEntity
): ModelEntity()
