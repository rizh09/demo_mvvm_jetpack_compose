package com.example.demo_mvvm_jetpack_compose.presentation.viewmodel

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.demo_mvvm_jetpack_compose.data.database.DatabaseQuoteResult
import com.example.demo_mvvm_jetpack_compose.data.database.asDomainModel
import com.example.demo_mvvm_jetpack_compose.data.model.Tags
import com.example.demo_mvvm_jetpack_compose.domain.GetPagingQuoteUseCase
import com.example.demo_mvvm_jetpack_compose.domain.GetQuoteTagUseCase
import com.example.demo_mvvm_jetpack_compose.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuoteTagViewModel @Inject constructor(
    private val getQuoteTagUseCase: GetQuoteTagUseCase,
    private val getPagingQuoteUseCase: GetPagingQuoteUseCase
) : ViewModel() {

    init {
        loadTagData()
    }

    //https://stackoverflow.com/questions/63146318/how-to-create-and-use-a-room-database-in-kotlin-dagger-hilt
    private val _isLoading = MutableStateFlow(false)
    private val _userMessage = MutableStateFlow("")
    private val _quoteResultAsync = getQuoteTagUseCase.quoteTags.asFlow().map {
        Async.Success(it)
    }.onStart<Async<List<Tags.TagsItem>>> { emit(Async.Loading) }

    private val _uiState = MutableStateFlow<QuoteTagUiState>(
        QuoteTagUiState()
    )

    //uiState as a object between viewModel and activity
    val uiState: StateFlow<QuoteTagUiState> = combine(
        _isLoading, _userMessage, _quoteResultAsync
    ) { isLoading, userMessage, quoteTagAsync ->
        when (quoteTagAsync) {
            Async.Loading -> {
                QuoteTagUiState(isLoading = true)
            }
            is Async.Success -> {
                QuoteTagUiState(
                    quoteTags = quoteTagAsync.data,
                    isLoading = isLoading,
                    userMessage = userMessage
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = QuoteTagUiState(isLoading = true)
    )

    private val _data: MutableLiveData<List<Tags.TagsItem>> by lazy {
        MutableLiveData<List<Tags.TagsItem>>().also {
            loadTagData()
        }
    }

    // it refers to repository's data, it ensure the UI load data from UI
    fun getData(): LiveData<List<Tags.TagsItem>> {
        return getQuoteTagUseCase.quoteTags
    }

    private fun loadTagData() {
        // Do an asynchronous operation to fetch users
        viewModelScope.launch(Dispatchers.IO) {
            getQuoteTagUseCase.invoke()
        }
    }

    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            getQuoteTagUseCase.invoke()
            _isLoading.value = false
        }
    }


    //show paging data by selected tag
    private val _pagingData = MutableStateFlow<PagingData<DatabaseQuoteResult>>(PagingData.empty())

    val pagingData = _pagingData.map { pagingData ->
        pagingData.map {
            it.asDomainModel()
        }
    }

    suspend fun getSingleListOfQuoteByTag(tag: String) {
        viewModelScope.launch {
            getPagingQuoteUseCase.invoke(tag).cachedIn(viewModelScope).collect {
                _pagingData.value = it
            }
        }
    }
}

data class QuoteTagUiState(
    val isLoading: Boolean = false,
    val quoteTags: List<Tags.TagsItem> = emptyList(),
    val userMessage: String? = null
)
