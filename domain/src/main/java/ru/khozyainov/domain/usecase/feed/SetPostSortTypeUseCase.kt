package ru.khozyainov.domain.usecase.feed

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.khozyainov.domain.model.PostSortType
import ru.khozyainov.domain.repo.PostRepository

class SetPostSortTypeUseCase(
    private val postRepository: PostRepository
) {
    suspend operator fun invoke(postSortType: PostSortType) = execute(postSortType = postSortType)
    private suspend fun execute(postSortType: PostSortType) = withContext(Dispatchers.IO) {
        postRepository.setSortType(sortType = postSortType)
    }
}