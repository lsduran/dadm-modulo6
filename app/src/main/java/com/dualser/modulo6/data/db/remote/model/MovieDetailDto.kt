package com.dualser.modulo6.data.db.remote.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class MovieDetailDto(
    @SerializedName("id")
    var id: Long? = null,
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("genre")
    var genre: String? = null,
    @SerializedName("rating")
    var rating: Float = 0f,
    @SerializedName("cover_url")
    var coverUrl: String? = null,
    @SerializedName("director")
    var director: String? = null,
    @SerializedName("description")
    var description: String? = null,
    @SerializedName("starring")
    var starring: List<String>? = null,
    @SerializedName("platforms")
    var platforms: List<String>? = null,
    @SerializedName("distributed_by")
    var distributedBy: String? = null,
    @SerializedName("release_date")
    var releaseDate: Date? = null
)
