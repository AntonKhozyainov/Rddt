package ru.khozyainov.domain.usecase.feed

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.khozyainov.domain.model.PostSortType
import ru.khozyainov.domain.repo.PostRepository

@OptIn(ExperimentalCoroutinesApi::class)
class SetPostSortTypeUseCaseTest {

    private val postRepository = mock<PostRepository>()

    @Test
    fun shouldRunWithoutException() = runTest {
        //given
        val testPostSortType = PostSortType(sort = "HOT")

        //when
        whenever(postRepository.setSortType(sortType = testPostSortType))

        //then
        SetPostSortTypeUseCase(postRepository = postRepository).invoke(postSortType = testPostSortType)
    }
}