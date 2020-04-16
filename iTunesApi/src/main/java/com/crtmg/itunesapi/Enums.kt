package com.crtmg.itunesapi

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