package com.example.demo_mvvm_jetpack_compose.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.demo_mvvm_jetpack_compose.database.asDomainModel
import com.example.demo_mvvm_jetpack_compose.di.module.QuoteResultDao
import com.example.demo_mvvm_jetpack_compose.model.Quote
import com.example.demo_mvvm_jetpack_compose.network.internet.QuoteService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


//purpose of repository pattern
//A repository can resolve conflicts between data sources (such as persistent models, web services, and caches) and centralize changes to this data.
//ref https://developer.android.com/codelabs/basic-android-kotlin-training-repository-pattern#3


//https://github.com/KadirKuruca/NewsApp-MVVM-Hilt-Room-Retrofit/blob/master/app/src/main/java/com/kadirkuruca/newsapp/repository/NewsRepository.kt
class QuoteResultRepository @Inject constructor(
    private val quoteResultDao: QuoteResultDao,
    private val quoteService: QuoteService
) {
    //ref: https://github.com/google-developer-training/android-kotlin-fundamentals-apps/blob/master/RepositoryPattern/app/src/main/java/com/example/android/devbyteviewer/repository/VideosRepository.kt
    val quoteResults: LiveData<List<Quote.Result>> =
        Transformations.map(quoteResultDao.getQuoteResult()) {
            it.asDomainModel()
        }

    suspend fun refreshQuotesResult() {
        //purpose of this function : refresh offline cache

        //perform async avoid blocking the main thread
        withContext(Dispatchers.IO) {
            //api call
            val quoteBody = quoteService.getQuotes().body()
            quoteBody.let {
                if (it != null) {
                    quoteResultDao.insertAll(it.asDomainModel())
                }
            }
        }
    }
}