package com.example.android.networkconnect.presentation.episode_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.networkconnect.data.models.character.toCharacterUIModel
import com.example.android.networkconnect.support.repo.AppRepository
import com.example.android.networkconnect.support.uimodel.CharacterUIModel
import com.example.android.networkconnect.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class EpisodeDetailViewModel @Inject constructor(var repo: AppRepository) : ViewModel(){

    private val _episodeCharacterList = MutableStateFlow<List<CharacterUIModel>>(emptyList())
    val episodeCharacterList = _episodeCharacterList.asStateFlow()

    private val _characterIds = MutableStateFlow("")
    val characterIds = _characterIds.asStateFlow()

    private val _episodeLoading = MutableStateFlow(false)
    val episodeLoading = _episodeLoading.asStateFlow()

    private val _episodeError = MutableStateFlow("")
    val episodeError = _episodeError.asStateFlow()


    fun getCharacterUrlList(urls : List<String>){
        parseCharacterId(urls)
        getEpisodeCharacters()
    }

    private fun getEpisodeCharacters() {
        val response = getEpisodeCharactersNetworkResult()
        response.onEach{ networkResult->
            when(networkResult){
                is NetworkResult.Loading ->{_episodeLoading.value = true}
                is NetworkResult.Success ->{
                    networkResult.data?.let {
                        _episodeCharacterList.value = it//list hazır
                    }
                    _episodeLoading.value = false
                }
                is NetworkResult.Error ->{
                    _episodeLoading.value = false
                    _episodeError.value = "Error!!"
                }
            }
        }.launchIn(viewModelScope)

    }

    private fun getEpisodeCharactersNetworkResult() : Flow<NetworkResult<List<CharacterUIModel>>> = flow {
        try {
            emit(NetworkResult.Loading())
            val response = repo.getCharacterByIdList(characterIds.value)
            if(response.isNotEmpty()){
                emit(NetworkResult.Success(response.map { character ->
                    character.toCharacterUIModel()
                }))
            }else{
                emit(NetworkResult.Error("No Data!!"))
            }
        }catch (e: HttpException) {
            emit(NetworkResult.Error(e.localizedMessage.orEmpty()))
        } catch (e: IOException) {
            emit(NetworkResult.Error(e.localizedMessage.orEmpty()))
        }
    }


    private fun parseCharacterId(urls : List<String>){
        urls.forEach { url ->
            val id = url.substringAfter("character/")
            _characterIds.value += "$id,"
        }
        _characterIds.value = _characterIds.value.substring(0,_characterIds.value.length-1)
    }
}