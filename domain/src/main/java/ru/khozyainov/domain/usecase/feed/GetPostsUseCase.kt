package ru.khozyainov.domain.usecase.feed

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.khozyainov.domain.model.Post
import ru.khozyainov.domain.model.PostRequestArgs
import ru.khozyainov.domain.model.PostSortType
import ru.khozyainov.domain.repo.PostRepository

class GetPostsUseCase(
    private val postRepository: PostRepository
) {

    operator fun invoke(args: PostRequestArgs) : Flow<PagingData<Post>>
        = postRepository.getPosts(args = args)
}