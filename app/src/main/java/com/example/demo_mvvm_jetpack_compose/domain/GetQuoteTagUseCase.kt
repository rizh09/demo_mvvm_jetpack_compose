package com.example.demo_mvvm_jetpack_compose.domain

import androidx.lifecycle.asLiveData
import com.example.demo_mvvm_jetpack_compose.data.repository.QuoteTagRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetQuoteTagUseCase @Inject constructor(
    private val quoteTagRepository: QuoteTagRepository
) {
    val quoteTags = quoteTagRepository.getQuoteTags().asLiveData()

    suspend operator fun invoke() =
        withContext(Dispatchers.IO) {
            quoteTagRepository.refreshLocalDataByAPICall()
        }
}
