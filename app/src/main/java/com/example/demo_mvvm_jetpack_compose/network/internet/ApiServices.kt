package com.example.demo_mvvm_jetpack_compose.network.internet

import com.example.demo_mvvm_jetpack_compose.data.model.Quote
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

//purpose of QuoteService
//it defines endpoint to make api requests
interface QuoteService {
    //it can perform its work without blocking and return the result as a list:
    //ref : https://kotlinlang.org/docs/flow.html#suspending-functions


    //hard code page = 1, atm
    @GET("quotes?page=1")
    suspend fun getQuotes(): Response<Quote>

    @GET("quotes")
    suspend fun getQuotesByPageAndKeywords(
        @Query("page") loadKey: String? = null,
        @Query("tags") keywords: String? = null
    ): Response<Quote>


}
