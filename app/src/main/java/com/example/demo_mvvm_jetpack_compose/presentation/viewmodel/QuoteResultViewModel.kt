package com.example.demo_mvvm_jetpack_compose.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.demo_mvvm_jetpack_compose.data.database.DatabaseQuoteResult
import com.example.demo_mvvm_jetpack_compose.data.database.asDomainModel
import com.example.demo_mvvm_jetpack_compose.data.model.Quote
import com.example.demo_mvvm_jetpack_compose.domain.GetPagingQuoteUseCase
import com.example.demo_mvvm_jetpack_compose.domain.GetQuoteUseCase
import com.example.demo_mvvm_jetpack_compose.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject


//With the @Inject annotation, Dagger knows:
//
//1. How to create instances of type RegistrationViewModel.
//2. RegistrationViewModel has UserManager as dependency since the constructor takes an instance of UserManager as an argument.
@HiltViewModel
class QuoteResultViewModel @Inject constructor(
    private val getQuoteUseCase: GetQuoteUseCase,
    private val getPagingQuoteUseCase: GetPagingQuoteUseCase
) :
    ViewModel() {
    //https://stackoverflow.com/questions/63146318/how-to-create-and-use-a-room-database-in-kotlin-dagger-hilt

    private val _isLoading = MutableStateFlow(false)
    private val _userMessage = MutableStateFlow("")
    private val _quoteResultAsync = getQuoteUseCase.quoteResultList.asFlow().map {
        Async.Success(it)
    }.onStart<Async<List<Quote.Result>>> { emit(Async.Loading) }

    private val _uiState = MutableStateFlow<QuoteResultUiState>(
        QuoteResultUiState()
    )

    //uiState as a object between viewModel and activity
    val uiState: StateFlow<QuoteResultUiState> = combine(
        _isLoading, _userMessage, _quoteResultAsync
    ) { isLoading, userMessage, quoteResultAsync ->
        when (quoteResultAsync) {
            Async.Loading -> {
                QuoteResultUiState(isLoading = true)
            }
            is Async.Success -> {
                QuoteResultUiState(
                    quoteResults = quoteResultAsync.data,
                    isLoading = isLoading,
                    userMessage = userMessage
                )
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = WhileSubscribed(5000),
        initialValue = QuoteResultUiState(isLoading = true)
    )

    private val _data: MutableLiveData<List<Quote.Result>> by lazy {
        MutableLiveData<List<Quote.Result>>().also {
            loadData()
        }
    }

    // it refers to repository's data, it ensure the UI load data from UI
    fun getData(): LiveData<List<Quote.Result>> {
        return getQuoteUseCase.quoteResultList
    }

    private fun loadData() {
        // Do an asynchronous operation to fetch users
        viewModelScope.launch(Dispatchers.IO) {
            getQuoteUseCase.invoke()
            //after api call
        }
    }

    fun refresh() {
        _isLoading.value = true
        viewModelScope.launch {
            getQuoteUseCase.invoke()
            _isLoading.value = false
        }
    }

    //paging
    private val _pagingData = getPagingQuoteUseCase.quoteResultPaging.cachedIn(viewModelScope)

    val pagingData = _pagingData.map { pagingData ->
        pagingData.map {
            it.asDomainModel()
        }
    }

    //search query
    private val _searchQuery = mutableStateOf("")
    val searchQuery = _searchQuery

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    //search results
    private val _searchedQuotes =
        MutableStateFlow<PagingData<DatabaseQuoteResult>>(PagingData.empty())
    val searchedQuotes = _searchedQuotes.map { pagingData ->
        pagingData.map {
            it.asDomainModel()
        }
    }

    fun searchByKeywords(keywords: String) {
        viewModelScope.launch {
            getPagingQuoteUseCase.invoke(keywords).cachedIn(viewModelScope).collect {

                _searchedQuotes.value = it
            }
        }
    }
}

data class QuoteResultUiState(
    val isLoading: Boolean = false,
    val quoteResults: List<Quote.Result> = emptyList(),
    val userMessage: String? = null
)
