package com.app.marvel.network

import com.app.marvel.data.Comic
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.lang.StringBuilder

@JsonClass(generateAdapter = true)
internal data class ComicNetworkWrapper(
    @Json(name = "code") val code: Int,
    @Json(name = "attributionText") val attributionText: String,
    @Json(name = "data") val data: ComicDataContainer
)

@JsonClass(generateAdapter = true)
internal data class ComicDataContainer(
    @Json(name = "results") val results: List<ComicNetworkResults>
)

@JsonClass(generateAdapter = true)
internal data class ComicNetworkResults(
    @Json(name = "id") val _id: Int,
    @Json(name = "issueNumber") val _issueNumber: Double,
    @Json(name  = "title") val _title: String,
    @Json(name = "description") val _description: String,
    @Json(name = "thumbnail") val _thumbnail: ComicThumbnail
) {
    fun toComic(): Comic {
        return object : Comic {
            override val id: Int
                get() = _id
            override val issueNumber: Double
                get() = _issueNumber
            override val title: String
                get() = _title
            override val description: String
                get() = _description
            override val thumbnail: String
                get() = _thumbnail.getFullUrl()
        }
    }
}

@JsonClass(generateAdapter = true)
internal data class ComicThumbnail(
    @Json(name = "path") val path: String,
    @Json(name = "extension") val ext: String
) {
    fun getFullUrl(): String = StringBuilder(path.replace("http", "https")).append(".").append(ext).toString()
}