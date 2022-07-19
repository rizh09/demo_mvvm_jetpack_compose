package com.example.demo_mvvm_jetpack_compose.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.demo_mvvm_jetpack_compose.model.Quote


/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */


/**
 * DatabaseVideo represents a video entity in the database.
 */
@Entity
data class DatabaseQuoteResult constructor(
    @PrimaryKey
    val id: String,
    val author: String,
    val authorSlug: String,
    val content: String,
    val tags: String,
    val dateAdded: String,
    val dateModified: String,
    val length: Int
)

/**
 * Map DatabaseVideos to domain entities
 */
fun Quote.asDomainModel(): List<DatabaseQuoteResult> {
    return this.results.map { it ->
        DatabaseQuoteResult(
            id = it.id,
            author = it.author,
            authorSlug = it.authorSlug,
            content = it.content,
            //from list to string
            tags = it.tags.joinToString {
                it
            },
            dateAdded = it.dateAdded,
            dateModified = it.dateModified,
            length = it.length
        )
    }
}

//from database model "DatabaseQuoteResult" to response model "Quote.Result"
fun List<DatabaseQuoteResult>.asDomainModel(): List<Quote.Result> {
    return map {
        Quote.Result(
            id = it.id,
            author = it.author,
            authorSlug = it.authorSlug,
            content = it.content,
            //from string to list
            tags = it.tags.split(","),
            dateAdded = it.dateAdded,
            dateModified = it.dateModified,
            length = it.length
        )
    }
}


fun DatabaseQuoteResult.asDomainModel(): Quote.Result {
    return this.let {
        Quote.Result(
            id = it.id,
            author = it.author,
            authorSlug = it.authorSlug,
            content = it.content,
            //from string to list
            tags = it.tags.split(","),
            dateAdded = it.dateAdded,
            dateModified = it.dateModified,
            length = it.length
        )
    }
}
