package com.example.demo_mvvm_jetpack_compose.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.demo_mvvm_jetpack_compose.domain.GetQuoteUseCase
import com.example.demo_mvvm_jetpack_compose.model.Quote
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


//With the @Inject annotation, Dagger knows:
//
//1. How to create instances of type RegistrationViewModel.
//2. RegistrationViewModel has UserManager as dependency since the constructor takes an instance of UserManager as an argument.
@HiltViewModel
class QuoteResultViewModel @Inject constructor(
    private val getQuoteUseCase: GetQuoteUseCase
) :
    ViewModel() {
    //https://stackoverflow.com/questions/63146318/how-to-create-and-use-a-room-database-in-kotlin-dagger-hilt

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
        // Do an asynchronous operation to fetch users.
        viewModelScope.launch(Dispatchers.IO) {
            getQuoteUseCase.invoke()
        }
    }
}
