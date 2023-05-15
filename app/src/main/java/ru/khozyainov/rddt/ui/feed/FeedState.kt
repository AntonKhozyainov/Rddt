package ru.khozyainov.rddt.ui.feed

import ru.khozyainov.rddt.model.UiPostSortType

sealed class FeedState {
    data class Default(
        val sortType: UiPostSortType = UiPostSortType.HOT
    ) : FeedState()

    object Loading : FeedState()
    data class Error(val exception: Throwable) : FeedState()
}
