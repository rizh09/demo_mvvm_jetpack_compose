package com.example.demo_mvvm_jetpack_compose.domain

import com.example.demo_mvvm_jetpack_compose.data.repository.QuoteResultPagingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetPagingQuoteUseCase @Inject constructor(
    private val quoteResultPagingRepository: QuoteResultPagingRepository
) {

    val quoteResultPaging = quoteResultPagingRepository.getDataOfQuoteResult()

    suspend operator fun invoke(keywords: String) = withContext(Dispatchers.IO)
    {
        quoteResultPagingRepository.getDataOfQuoteResultByKeywords(keywords = keywords)
    }
}
