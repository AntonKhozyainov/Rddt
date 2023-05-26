package ru.khozyainov.rddt.ui.feed

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.khozyainov.rddt.model.UiModelPost
import ru.khozyainov.rddt.model.UiPostSortType

//sealed class FeedState {
//    data class Default(
//        val sortType: UiPostSortType = UiPostSortType.HOT,
//        val query: String
//    ) : FeedState()
//
//    object Loading : FeedState()
//    data class Error(val exception: Throwable) : FeedState()
//}

data class FeedState(
    val sortType: UiPostSortType = UiPostSortType.HOT,
    val query: String = ""
)

