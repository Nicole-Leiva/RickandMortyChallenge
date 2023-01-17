package com.example.android.networkconnect.support.repo

import androidx.paging.PagingData
import com.example.android.networkconnect.data.models.character.CharacterResponse
import com.example.android.networkconnect.data.models.episode.Episode
import com.example.android.networkconnect.data.models.character.Character
import com.example.android.networkconnect.data.models.episode.EpisodeResponse
import kotlinx.coroutines.flow.Flow

interface AppRepository {

    suspend fun getCharacters(): Flow<PagingData<Character>>

    suspend fun getAllEpisodes(): Flow<PagingData<Episode>>

    suspend fun getFilterCharacters(filterQuery: Map<String,String>): Flow<PagingData<Character>>

    suspend fun getFilterEpisodes(filter: String): Flow<PagingData<Episode>>

    suspend fun getCharactersNetworkResult(pageNumber : Int) : CharacterResponse
    suspend fun getEpisodesNetworkResult(pageNumber : Int) : EpisodeResponse

    suspend fun getCharacterByIdList(ids:String): List<Character>

}