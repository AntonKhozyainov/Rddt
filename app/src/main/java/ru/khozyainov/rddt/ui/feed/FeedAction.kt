package ru.khozyainov.rddt.ui.feed

import ru.khozyainov.rddt.model.UiPostSortType

sealed class FeedAction { //TODO is delete?
    data class Search(val query: String) : FeedAction()
    data class Scroll(val currentQuery: String) : FeedAction()
    data class Sort(val sort: UiPostSortType) : FeedAction()
}