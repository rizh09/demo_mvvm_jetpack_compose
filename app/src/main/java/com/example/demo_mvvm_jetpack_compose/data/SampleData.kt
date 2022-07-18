package com.example.demo_mvvm_jetpack_compose.data

import com.example.demo_mvvm_jetpack_compose.model.Quote

//hard code for testing list/ detail navigation

object QuoteResultData {
    val quoteResults: List<Quote.Result> = listOf(
        Quote.Result(
            id = "1",
            author = "Chau Ki KO",
            authorSlug = "a",
            content = "a",
            //from string to list
            tags = "a,b,c".split(","),
            dateAdded = "asd",
            dateModified = "asd",
            length = 123
        ),
        Quote.Result(
            id = "1",
            author = "Chau Ki KO",
            authorSlug = "a",
            content = "a",
            //from string to list
            tags = "a,b,c".split(","),
            dateAdded = "asd",
            dateModified = "asd",
            length = 123
        ),
        Quote.Result(
            id = "1",
            author = "Chau Ki KO",
            authorSlug = "a",
            content = "a",
            //from string to list
            tags = "a,b,c".split(","),
            dateAdded = "asd",
            dateModified = "asd",
            length = 123
        ),
    )

    fun getSingleQuoteResult(ID: String?): Quote.Result {
        return quoteResults.first { it.id == ID }
    }
}
