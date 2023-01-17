package com.example.android.networkconnect.data.retrofit


import com.example.android.networkconnect.data.models.character.CharacterResponse
import com.example.android.networkconnect.data.models.episode.EpisodeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import retrofit2.http.QueryMap
import com.example.android.networkconnect.data.models.character.Character

interface AppRemoteDao {

    @GET("character")
    suspend fun getAllCharacters(@Query("page") query: Int): CharacterResponse

    @GET("episode")
    suspend fun getAllEpisodes(@Query("page") query: Int): EpisodeResponse

    @GET("character")
    suspend fun getFilterCharacters(@Query("page") query: Int, @QueryMap filterQuery: Map<String, String>): CharacterResponse

    @GET("episode")
    suspend fun getFilterEpisodes(@Query("page") query: Int, @Query("episode") filter: String): EpisodeResponse

    @GET("character/{ids}")
    suspend fun getCharacterByIdList(@Path("ids") ids: String): List<Character>

}