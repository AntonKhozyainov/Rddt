package ru.khozyainov.domain.usecase.feed

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import ru.khozyainov.domain.model.PostSortType
import ru.khozyainov.domain.repo.PostRepository

@OptIn(ExperimentalCoroutinesApi::class)
class GetPostSortTypeUseCaseTest {

    private val postRepository = mock<PostRepository>()

    @Test
    fun shouldReturnCorrectPostSorType() = runTest {
        //given
        val expected = PostSortType(sort = "NEW")

        //when
        whenever(postRepository.getSortType()).thenReturn(flow { emit(expected) })
        val actual = GetPostSortTypeUseCase(postRepository = postRepository).invoke().first()

        //then
        assertEquals(expected, actual)
    }
}