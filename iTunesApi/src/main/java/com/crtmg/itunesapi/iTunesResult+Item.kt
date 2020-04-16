package com.crtmg.itunesapi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration


@Serializable
data class iTunesItem(
    val wrapperType: String? = null,
    val kind: String? = null,

    @SerialName("trackId")
    val trackID: Long? = null,

    val artistName: String? = null,
    val trackName: String? = null,
    val trackCensoredName: String? = null,

    @SerialName("trackViewUrl")
    val trackViewURL: String? = null,

    @SerialName("previewUrl")
    val previewURL: String? = null,

    val artworkUrl30: String? = null,
    val artworkUrl60: String? = null,
    val artworkUrl100: String? = null,
    val collectionPrice: Double? = null,
    val trackPrice: Double? = null,

    @SerialName("collectionHdPrice")
    val collectionHDPrice: Double? = null,

    @SerialName("trackHdPrice")
    val trackHDPrice: Double? = null,

    val releaseDate: String? = null,
    val collectionExplicitness: String? = null,
    val trackExplicitness: String? = null,
    val trackTimeMillis: Long? = null,
    val country: String? = null,
    val currency: String? = null,
    val primaryGenreName: String? = null,
    val contentAdvisoryRating: String? = null,
    val shortDescription: String? = null,
    val longDescription: String? = null,
    val hasITunesExtras: Boolean? = null
) {
    val jsonString: String
        get() {
            val json = Json(JsonConfiguration(ignoreUnknownKeys = true))
            return json.stringify(serializer(), this)
        }

    companion object{
        fun createFrom(string: String): iTunesItem {
            val json = Json(JsonConfiguration(ignoreUnknownKeys = true))
            return json.parse(serializer(), string)
        }
    }
}

@Serializable
data class iTunesResult(
    val resultCount: Long,
    val results: List<iTunesItem>
)