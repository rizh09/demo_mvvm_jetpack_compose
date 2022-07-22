package com.example.demo_mvvm_jetpack_compose.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


//Purpose of Quote
//it demos how to implement moshi with generatedAdapter = true
@JsonClass(generateAdapter = true)
data class Quote(
    @Json(name = "count")
    val count: Int,
    @Json(name = "lastItemIndex")
    val lastItemIndex: Int,
    @Json(name = "page")
    val page: Int,
    @Json(name = "results")
    val results: List<Result>,
    @Json(name = "totalCount")
    val totalCount: Int,
    @Json(name = "totalPages")
    val totalPages: Int
) {
    @JsonClass(generateAdapter = true)
    data class Result(
        @Json(name = "author")
        val author: String,
        @Json(name = "authorSlug")
        val authorSlug: String,
        @Json(name = "content")
        val content: String,
        @Json(name = "dateAdded")
        val dateAdded: String,
        @Json(name = "dateModified")
        val dateModified: String,
        @Json(name = "_id")
        val id: String,
        @Json(name = "length")
        val length: Int,
        @Json(name = "tags")
        val tags: List<String>
    )
}
