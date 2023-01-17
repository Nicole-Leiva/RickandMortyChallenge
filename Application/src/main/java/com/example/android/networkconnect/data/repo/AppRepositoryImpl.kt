package com.example.android.networkconnect.data.repo

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.android.networkconnect.data.models.character.CharacterResponse
import com.example.android.networkconnect.data.models.episode.Episode
import com.example.android.networkconnect.data.models.character.Character
import com.example.android.networkconnect.data.models.episode.EpisodeResponse
import com.example.android.networkconnect.data.repo.pagingsource.CharacterPagingSource
import com.example.android.networkconnect.data.repo.pagingsource.EpisodePagingSource
import com.example.android.networkconnect.data.repo.pagingsource.FilterCharacterPagingSource
import com.example.android.networkconnect.data.repo.pagingsource.FilterEpisodePagingSource
import com.example.android.networkconnect.data.retrofit.AppRemoteDao
import com.example.android.networkconnect.support.repo.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(var remoteDao: AppRemoteDao) : AppRepository {

    override suspend fun getCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(42),
            pagingSourceFactory = { CharacterPagingSource(remoteDao) }).flow
    }

    override suspend fun getAllEpisodes(): Flow<PagingData<Episode>> {
        return Pager(
            config = PagingConfig(3),
            pagingSourceFactory = { EpisodePagingSource(remoteDao) }).flow
    }

    override suspend fun getFilterCharacters(filterQuery: Map<String, String>): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { FilterCharacterPagingSource(remoteDao,filterQuery) }).flow
    }

    override suspend fun getFilterEpisodes(filter: String): Flow<PagingData<Episode>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { FilterEpisodePagingSource(remoteDao, filter) }).flow
    }

    override suspend fun getCharactersNetworkResult(pageNumber: Int): CharacterResponse {
        return remoteDao.getAllCharacters(pageNumber)
    }

    override suspend fun getEpisodesNetworkResult(pageNumber: Int): EpisodeResponse {
        return  remoteDao.getAllEpisodes(pageNumber)
    }

    override suspend fun getCharacterByIdList(ids: String): List<Character> {
        return remoteDao.getCharacterByIdList(ids)
    }

}