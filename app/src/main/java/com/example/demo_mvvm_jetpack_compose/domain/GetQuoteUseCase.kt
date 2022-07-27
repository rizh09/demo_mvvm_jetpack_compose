package com.example.demo_mvvm_jetpack_compose.domain

import androidx.lifecycle.asLiveData
import com.example.demo_mvvm_jetpack_compose.data.repository.QuoteResultRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetQuoteUseCase @Inject constructor(
    private val quoteResultRepository: QuoteResultRepository
) {
    val quoteResultList = quoteResultRepository.getQuoteResults().asLiveData()

    suspend operator fun invoke() =
        withContext(Dispatchers.IO) {
            quoteResultRepository.refreshLocalDataByAPICall()
        }
}
