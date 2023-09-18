package com.dualser.modulo6.data.db.remote.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class MovieDto(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("genre")
    var genre: String? = null,
    @SerializedName("rating")
    var rating: Float? = null,
    @SerializedName("cover_url")
    var coverUrl: String? = null,
    @SerializedName("platforms")
    var platforms: List<String>? = null,
    @SerializedName("release_date")
    var releaseDate: Date? = null
)
