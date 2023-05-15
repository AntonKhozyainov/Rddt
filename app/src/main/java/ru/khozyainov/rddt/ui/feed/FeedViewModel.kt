package ru.khozyainov.rddt.ui.feed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import ru.khozyainov.domain.usecase.feed.GetPostSortTypeUseCase
import ru.khozyainov.domain.usecase.feed.SetPostSortTypeUseCase
import ru.khozyainov.rddt.mapper.UiPostSortTypeMapper
import ru.khozyainov.rddt.model.UiPostSortType

class FeedViewModel(
    private val getPostSortTypeUseCase: GetPostSortTypeUseCase,
    private val setPostSortTypeUseCase: SetPostSortTypeUseCase,
    private val uiPostSortTypeMapper: UiPostSortTypeMapper
):ViewModel() {

    private val uiMutableState = MutableStateFlow<FeedState>(FeedState.Loading)
    val uiState : StateFlow<FeedState> = uiMutableState

    private val errorHandler = CoroutineExceptionHandler { _, throwable ->
        uiMutableState.value = FeedState.Error(exception = throwable)
    }

    private var job: Job? = null

    init {
        refreshState()
    }

    fun refreshState(){
        job = getPostSortTypeUseCase()
            .onEach { postSortType ->
                uiMutableState.value = FeedState.Default(
                    uiPostSortTypeMapper.mapToUi(postSortType = postSortType)
                )
            }
            .flowOn(Dispatchers.IO)
            .catch { exception ->
                uiMutableState.value = FeedState.Error(
                    exception = exception
                )
            }
            .launchIn(viewModelScope)
    }

    fun setPostSortType(sortType: UiPostSortType){
        viewModelScope.launch(errorHandler) {
            setPostSortTypeUseCase(postSortType = uiPostSortTypeMapper.mapToDomain(uiPostSortType = sortType))
        }
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}