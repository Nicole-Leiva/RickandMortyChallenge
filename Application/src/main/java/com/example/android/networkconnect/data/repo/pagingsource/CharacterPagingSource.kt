package com.example.android.networkconnect.data.repo.pagingsource

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.networkconnect.data.retrofit.AppRemoteDao
import com.example.android.networkconnect.utils.Constants.FIRST_PAGE_INDEX
import javax.inject.Inject
import com.example.android.networkconnect.data.models.character.Character


class CharacterPagingSource @Inject constructor(var remoteDao: AppRemoteDao) :
    PagingSource<Int, Character>() {
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val nextPage: Int = params.key ?: FIRST_PAGE_INDEX
            val response = remoteDao.getAllCharacters(nextPage)
            var nextPageNumber: Int? = null

            val totalPageCount = response.info.pages
            nextPageNumber = if(nextPage == totalPageCount){
                null
            }else{
                val uri = Uri.parse(response.info.next)
                val nextPageQuery = uri.getQueryParameter("page")
                nextPageQuery?.toInt()
            }

            LoadResult.Page(
                data = response.characters,
                prevKey = null,
                nextKey = nextPageNumber
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}