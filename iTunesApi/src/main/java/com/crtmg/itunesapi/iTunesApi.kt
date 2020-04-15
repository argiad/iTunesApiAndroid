package com.crtmg.itunesapi

import android.content.Context
import android.os.StrictMode
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.RetryPolicy
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashMap


object iTunesApi {

    var callback: iTunesApiCallback? = null

    private var _context: Context? = null

    fun initWith(context: Context): iTunesApi {
        _context = context

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        return this
    }

    fun search(request: iTunesSearchRequest) {
        _context?.let {
            iTunesRequestManager.search(request, it)
        } ?: run {
            callback?.error(-1, "No context found")
        }
    }

}

internal object Constants {

    const val iTunesPath = "https://itunes.apple.com/"
    const val iTunesPathSearchComponent = "search"
    const val requestTimeOut = 10000
}

internal object iTunesRequestManager {

    fun search(request: iTunesSearchRequest, context: Context) {

        val queue = Volley.newRequestQueue(context)

        val inlineParams = "?" + request.parameters

        val jsonRequest = JsonObjectRequest(
            Request.Method.GET,
            Constants.iTunesPath + Constants.iTunesPathSearchComponent + inlineParams,
            null,
            Response.Listener {

//                println(it.toString())

                val json = Json(JsonConfiguration(ignoreUnknownKeys = true))
                val obj = json.parse(iTunesResult.serializer(), it.toString())
//                println(obj)
                iTunesApi.callback?.response(obj)
            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                iTunesApi.callback?.error(-2, error.toString())
            })

        val policy: RetryPolicy = DefaultRetryPolicy(
            request.requestTimeOut,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        jsonRequest.retryPolicy = policy
        queue.add(jsonRequest)

//        println(jsonRequest.url)

    }

}

data class iTunesSearchRequest(
    val term: String,
    val mediaType: MediaType = MediaType.all,
    val limit: Int = 50,
    val countryCode: String = Locale.getDefault().country,
    val requestTimeOut: Int = Constants.requestTimeOut
) {

    private val _limit = if (limit in 1..200) limit else if (limit < 1) 1 else 50

    internal val parameters: String
        get() {
            val result = HashMap<String, String>()

            result[ParameterKey.TERM.name.toLowerCase(Locale.getDefault())] = term
            result[ParameterKey.COUNTRY.name.toLowerCase(Locale.getDefault())] = countryCode
            result[ParameterKey.MEDIA.name.toLowerCase(Locale.getDefault())] = mediaType.name
            result[ParameterKey.LIMIT.name.toLowerCase(Locale.getDefault())] = _limit.toString()

            return result.entries.stream().map { it.key + "=" + it.value }
                .collect(Collectors.joining("&"))
        }

}

enum class ParameterKey {
    TERM,
    COUNTRY,
    MEDIA,
    ENTITY,
    ATTRIBUTE,
    LIMIT,
    LANG,
    VERSION,
    EXPLICIT;

}

enum class MediaType {
    movie,
    podcast,
    music,
    musicVideo,
    audiobook,
    shortFilm,
    tvShow,
    software,
    ebook,
    all;

    public val printableName: String
        get() {
            val pattern = Regex("(?=\\p{Lu})")
            return name.split(pattern).joinToString(" ") { it.capitalize() }
        }
}

public interface iTunesApiCallback {

    fun error(id: Int, description: String)
    fun response(result: iTunesResult)
}

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