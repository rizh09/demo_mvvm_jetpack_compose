package com.example.demo_mvvm_jetpack_compose.di.module

import android.util.Log
import com.example.demo_mvvm_jetpack_compose.util.Constant
import com.example.demo_mvvm_jetpack_compose.network.internet.QuoteService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

//Purpose of NetworkModule
//it demos how to implement Retrofit2 with hilt
@InstallIn(SingletonComponent::class)
@Module // @Module informs Dagger that this class is a Dagger Module
object NetworkModule {

    //@Provides tell Dagger how to create instances of the type that this function
    //Interfaces are not the only case where you cannot constructor-inject a type. Constructor injection is also not possible if you don't own the class because it comes from an external library (classes like Retrofit, OkHttpClient, or Room databases), or if instances must be created with the builder pattern.
    //ref: https://developer.android.com/training/dependency-injection/dagger-android
    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Constant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    @Provides
    fun provideQuoteRetrofitService(retrofit: Retrofit): QuoteService {
        return retrofit.create(QuoteService::class.java)
    }

    //for testing only
    @Provides
    fun provideDefaultDispatcher(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    @Provides
    fun provideOKHttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor { Log.d("API", it) }
        logger.setLevel(HttpLoggingInterceptor.Level.BASIC)
        return OkHttpClient.Builder().addInterceptor(logger).build()
    }

}
