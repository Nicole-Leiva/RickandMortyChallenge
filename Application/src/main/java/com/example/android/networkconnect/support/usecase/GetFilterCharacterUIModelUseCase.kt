package com.example.android.networkconnect.support.usecase

import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.android.networkconnect.data.models.character.toCharacterUIModel
import com.example.android.networkconnect.support.repo.AppRepository
import com.example.android.networkconnect.support.uimodel.CharacterUIModel
import com.example.android.networkconnect.utils.Constants.FIRST_PAGE_INDEX
import com.example.android.networkconnect.utils.NetworkResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetFilterCharacterUIModelUseCase @Inject constructor(var repo: AppRepository) {
    fun executeGetFilterCharacters(
        coroutineScope: CoroutineScope,
        filter: Map<String, String>
    ): Flow<NetworkResult<PagingData<CharacterUIModel>>> =
        flow {
            try {
                emit(NetworkResult.Loading())
                val response = repo.getCharactersNetworkResult(FIRST_PAGE_INDEX)
                if (response.characters.isNullOrEmpty()) {
                    emit(NetworkResult.Error(message = "No Rick And Morty Character"))
                } else {
                    repo.getFilterCharacters(filter).cachedIn(coroutineScope).collect {
                        emit(NetworkResult.Success(it.map { character ->
                            character.toCharacterUIModel()
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