package com.crtmg.itunesapi

import java.util.*
import java.util.stream.Collectors
import kotlin.collections.HashMap

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