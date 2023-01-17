package com.example.android.networkconnect.support.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.android.networkconnect.data.models.episode.toEpisodeUIModel
import com.example.android.networkconnect.support.repo.AppRepository
import com.example.android.networkconnect.support.uimodel.EpisodeUIModel
import com.example.android.networkconnect.utils.Constants
import com.example.android.networkconnect.utils.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetEpisodeUIModelUseCase @Inject constructor(var repo: AppRepository){
    fun executeGetEpisode(coroutineScope: CoroutineScope): Flow<NetworkResult<PagingData<EpisodeUIModel>>> = flow {
        try {
            emit(NetworkResult.Loading())
            val response = repo.getEpisodesNetworkResult(Constants.FIRST_PAGE_INDEX)
            if(response.episodes.isNullOrEmpty()){
                emit(NetworkResult.Error(message = "No Rick And Morty Episodes"))
            }else{
                repo.getAllEpisodes().cachedIn(coroutineScope).collect {
                    emit(NetworkResult.Success(it.map {
                            episode -> episode.toEpisodeUIModel()
                    }))
                }
            }
        } catch (e: HttpException) {
            emit(NetworkResult.Error(message = e.localizedMessage ?: "Error!"))
        } catch (e: IOException) {
            emit(NetworkResult.Error(message = "Could not reach URL"))
        }
    }
}