package com.example.demo_mvvm_jetpack_compose.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.demo_mvvm_jetpack_compose.data.model.Quote
import com.example.demo_mvvm_jetpack_compose.data.model.Tags


/**
 * Database entities go in this file. These are responsible for reading and writing from the
 * database.
 */


/**
 * DatabaseVideo represents a video entity in the database.
 */
@Entity(tableName = "quote_results")
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

@Entity(tableName = "remote_keys")
data class QuoteResultRemoteKey constructor(
    @PrimaryKey
    @ColumnInfo(collate = ColumnInfo.NOCASE)
    val quoteResultRemoteKey: String, // technically mutable but fine for a demo
    val nextPageKey: String?
)

@Entity(tableName = "quote_tags")
data class DatabaseQuoteTag constructor(
    @PrimaryKey
    val id: String,
    val name: String,
    val slug: String,
    val quoteCount: String,
    val dateAdded: String,
    val dateModified: String
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

@JvmName("asDomainModelDatabaseQuoteResult")
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

@JvmName("asDomainModelDatabaseQuoteTag")
fun List<DatabaseQuoteTag>.asDomainModel(): List<Tags.TagsItem> {
    return map {
        Tags.TagsItem(
            id = it.id,
            name = it.name,
            quoteCount = it.quoteCount,
            slug = it.slug,
            dateModified = it.dateModified,
            dateAdded = it.dateAdded
        )
    }
}

fun List<Tags.TagsItem>.asDomainModel(): List<DatabaseQuoteTag> {
    return map { tagItem ->
        DatabaseQuoteTag(
            id = tagItem.id,
            name = tagItem.name,
            quoteCount = tagItem.quoteCount,
            slug = tagItem.slug,
            dateModified = tagItem.dateModified,
            dateAdded = tagItem.dateAdded
        )
    }
}
