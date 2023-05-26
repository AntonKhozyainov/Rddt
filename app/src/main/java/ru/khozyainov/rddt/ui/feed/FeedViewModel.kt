package ru.khozyainov.rddt.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import ru.khozyainov.domain.model.PostRequestArgs
import ru.khozyainov.domain.usecase.feed.GetPostSortTypeUseCase
import ru.khozyainov.domain.usecase.feed.GetPostsUseCase
import ru.khozyainov.domain.usecase.feed.SetPostSortTypeUseCase
import ru.khozyainov.rddt.mapper.UiPostSortTypeMapper
import ru.khozyainov.rddt.model.UiModelPost
import ru.khozyainov.rddt.model.UiPostSortType

@OptIn(ExperimentalCoroutinesApi::class)
class FeedViewModel(
    getPostSortTypeUseCase: GetPostSortTypeUseCase,
    private val setPostSortTypeUseCase: SetPostSortTypeUseCase,
    private val getPostsUseCase: GetPostsUseCase,
    private val uiPostSortTypeMapper: UiPostSortTypeMapper
) : ViewModel() {

    //private val uiMutableState = MutableStateFlow<FeedState>(FeedState.Loading)
    val uiState: StateFlow<FeedState>// = uiMutableState
    val pagingDataFlow: Flow<PagingData<UiModelPost>>

    private val sort = getPostSortTypeUseCase()
        .mapLatest { postSortType ->
            uiPostSortTypeMapper.mapToUi(postSortType = postSortType)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = UiPostSortType.HOT
        )

    private val searchBy = MutableStateFlow(String())

    private var jobUpdateSearchSort: Job? = null

    init {

        pagingDataFlow = combine(
            searchBy,
            sort,
            ::Pair
        ).flatMapLatest { (query, sort) ->
            val requestArgs = PostRequestArgs(
                searchString = query,
                sort = uiPostSortTypeMapper.mapToDomain(uiPostSortType = sort)
            )
            getPostsUseCase(args = requestArgs).map { pagingData ->
                pagingData.map {post ->
                    UiModelPost.TextPost(id = post.id) as UiModelPost//TODO
                }
            }
        }.cachedIn(viewModelScope)

        uiState = combine(
            searchBy,
            sort,
            ::Pair
        ).map { (query, sort) ->
            FeedState(
                sortType = sort,
                query = query
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
            initialValue = FeedState()
        )
    }

    override fun onCleared() {
        jobUpdateSearchSort?.cancel()
        super.onCleared()
    }

    fun bindSearchAndSortState(queryFlow: Flow<String>, postSortFlow: Flow<UiPostSortType?>) {
        jobUpdateSearchSort = combine(
            queryFlow,
            postSortFlow
        ) { query, sort ->
            query to sort
        }
            .distinctUntilChanged()
            .onEach { (query, sort) ->
                if (sort != null) {
                    searchBy.value = query
                    setPostSortTypeUseCase(postSortType = uiPostSortTypeMapper.mapToDomain(uiPostSortType = sort))
                }
            }
            .launchIn(viewModelScope)
    }

}