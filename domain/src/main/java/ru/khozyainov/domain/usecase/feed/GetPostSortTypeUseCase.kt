package ru.khozyainov.domain.usecase.feed

import kotlinx.coroutines.flow.Flow
import ru.khozyainov.domain.model.PostSortType
import ru.khozyainov.domain.repo.PostRepository

class GetPostSortTypeUseCase(
    private val postRepository: PostRepository
) {
    operator fun invoke(): Flow<PostSortType> = postRepository.getSortType()
}