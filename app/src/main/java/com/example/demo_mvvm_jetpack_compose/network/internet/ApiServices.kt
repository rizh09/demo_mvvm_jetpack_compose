package com.example.demo_mvvm_jetpack_compose.network.internet

import com.example.demo_mvvm_jetpack_compose.data.model.Quote
import com.example.demo_mvvm_jetpack_compose.data.model.Tags
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//purpose of QuoteService
//it defines endpoint to make api requests
interface QuoteService {
    //it can perform its work without blocking and return the result as a list:
    //ref : https://kotlinlang.org/docs/flow.html#suspending-functions

    @GET("tags")
    suspend fun getTags(): Response<List<Tags.TagsItem>>

    @GET("quotes")
    suspend fun getRandomQuotesByPage(
        @Query("page") loadKey: String? = null
    ): Response<Quote>

    @GET("quotes")
    suspend fun getQuotesByPageAndKeywords(
        @Query("page") loadKey: String? = null,
        @Query("tags") keywords: String? = null
    ): Response<Quote>

    @GET("random")
    suspend fun getOneRandomQuote(): Response<Quote.Result>
}
