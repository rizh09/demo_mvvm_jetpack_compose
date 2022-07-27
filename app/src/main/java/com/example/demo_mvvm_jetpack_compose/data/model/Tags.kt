package com.example.demo_mvvm_jetpack_compose.data.model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

class Tags : ArrayList<Tags.TagsItem>() {
    @JsonClass(generateAdapter = true)
    data class TagsItem(
        @Json(name = "dateAdded")
        val dateAdded: String,
        @Json(name = "dateModified")
        val dateModified: String,
        @Json(name = "_id")
        val id: String,
        @Json(name = "name")
        val name: String,
        @Json(name = "quoteCount")
        val quoteCount: String,
        @Json(name = "slug")
        val slug: String
    )
}
