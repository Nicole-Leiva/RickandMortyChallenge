package com.example.android.networkconnect.data.models.episode

import com.example.android.networkconnect.support.uimodel.EpisodeUIModel
import com.google.gson.annotations.SerializedName

data class Episode(
    @SerializedName("air_date")
    val airDate: String,
    @SerializedName("characters")
    val characters: List<String>,
    @SerializedName("created")
    val created: String,
    @SerializedName("episode")
    val episode: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("url")
    val url: String
)

fun Episode.toEpisodeUIModel(): EpisodeUIModel {
    return EpisodeUIModel(
        id, name, airDate, characters, created, episode, url
    )
}