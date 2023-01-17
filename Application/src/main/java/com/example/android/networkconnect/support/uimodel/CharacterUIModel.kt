package com.example.android.networkconnect.support.uimodel

import com.example.android.networkconnect.data.models.character.Location
import com.example.android.networkconnect.data.models.character.Origin


data class CharacterUIModel(
    val id: Int,
    val name: String,
    val status: String,
    val gender: String,
    val species: String,
    val created: String,
    val episode: List<String>,
    val image: String,
    val location: Location,
    val origin: Origin,
    val url: String
)
