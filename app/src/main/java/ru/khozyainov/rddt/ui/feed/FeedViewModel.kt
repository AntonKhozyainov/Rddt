package ru.khozyainov.rddt.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.khozyainov.domain.usecase.feed.GetPostSortTypeUseCase
import ru.khozyainov.domain.usecase.feed.GetPostsUseCase
import ru.khozyainov.domain.usecase.feed.SetPostSortTypeUseCase
import ru.khozyainov.rddt.mapper.UiPostSortTypeMapper
import ru.khozyainov.rddt.model.UiPostSortType

class FeedViewModel(
    private val getPostSortTypeUseCase: GetPostSortTypeUseCase,
    private val setPostSortTypeUseCase: SetPostSortTypeUseCase,
    private val getPostsUseCase: GetPostsUseCase,
    private val uiPostSortTypeMapper: UiPostSortTypeMapper
) : ViewModel() {

    private val uiMutableState = MutableStateFlow<FeedState>(FeedState.Loading)
    val uiState: StateFlow<FeedState> = uiMutableState

//    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
//        uiMutableState.value = FeedState.Error(exception = throwable)
//    }

    private val searchBy = MutableStateFlow(String())
    private val currentSort = MutableStateFlow(UiPostSortType.HOT)

    private var jobGetPosts: Job? = null
    private var jobUpdateSearchSort: Job? = null

    init {
        refreshState()
    }

    override fun onCleared() {
        super.onCleared()
        jobGetPosts?.cancel()
        jobUpdateSearchSort?.cancel()
    }

    fun refreshState() {

        jobGetPosts = combine(
            searchBy,
            getPostSortTypeUseCase()
        ) { query, postSortType ->
            val sort = uiPostSortTypeMapper.mapToUi(postSortType = postSortType)
            currentSort.value = sort
            query to sort
        }
            .onEach { (query, sort) ->
                uiMutableState.value = FeedState.Default(
                    sortType = sort,
                    query = query
                )
            }
            .flowOn(Dispatchers.IO)
            .catch { exception ->
                uiMutableState.value = FeedState.Error(exception = exception)
            }
            .launchIn(viewModelScope)
    }

    fun updateSearchAndSortState(queryFlow: Flow<String>, postSortFlow: Flow<UiPostSortType?>) {
        jobUpdateSearchSort = combine(
            queryFlow,
            postSortFlow
        ) { query, sort ->
            query to sort
        }
            .distinctUntilChanged()
            .onEach { (query, sort) ->
                searchBy.value = query
                if (currentSort.value != sort && sort != null) {
                    setPostSortTypeUseCase(postSortType = uiPostSortTypeMapper.mapToDomain(uiPostSortType = sort))
                }
            }
            .catch { exception ->
                uiMutableState.value = FeedState.Error(exception = exception)
            }
            .launchIn(viewModelScope)
    }

}