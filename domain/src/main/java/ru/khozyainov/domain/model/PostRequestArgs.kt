package ru.khozyainov.domain.model

data class PostRequestArgs(
    val searchString: String,
    val sort: PostSortType
): Model()
