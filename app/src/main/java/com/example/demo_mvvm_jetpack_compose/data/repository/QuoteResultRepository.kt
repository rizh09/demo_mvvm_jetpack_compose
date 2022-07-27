package com.example.demo_mvvm_jetpack_compose.data.repository

import com.example.demo_mvvm_jetpack_compose.data.database.asDomainModel
import com.example.demo_mvvm_jetpack_compose.data.model.Quote
import com.example.demo_mvvm_jetpack_compose.di.module.QuoteResultDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton


//purpose of repository pattern, business logic resides repository. if repository pattern didnt apply reside in model
//A repository can resolve conflicts between data sources (such as persistent models, web services, and caches) and centralize changes to this data.
//ref https://developer.android.com/codelabs/basic-android-kotlin-training-repository-pattern#3


//https://github.com/KadirKuruca/NewsApp-MVVM-Hilt-Room-Retrofit/blob/master/app/src/main/java/com/kadirkuruca/newsapp/repository/NewsRepository.kt
@Singleton
class QuoteResultRepository @Inject constructor(
    private val quoteResultDao: QuoteResultDao,
    private val defaultDispatcher: CoroutineDispatcher
) {

    //ref: https://github.com/google-developer-training/android-kotlin-fundamentals-apps/blob/master/RepositoryPattern/app/src/main/java/com/example/android/devbyteviewer/repository/VideosRepository.kt

    //we can do Intermediate operation in Repository e.g filter
    fun getQuoteResults(): Flow<List<Quote.Result>> =
        quoteResultDao.getQuoteResult().map { it.asDomainModel() }.flowOn(defaultDispatcher)

}
