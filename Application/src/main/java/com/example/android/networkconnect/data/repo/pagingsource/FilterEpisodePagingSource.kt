package com.example.android.networkconnect.data.repo.pagingsource

import android.net.Uri
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.android.networkconnect.data.models.episode.Episode
import com.example.android.networkconnect.data.retrofit.AppRemoteDao
import com.example.android.networkconnect.utils.Constants
import javax.inject.Inject

class FilterEpisodePagingSource @Inject constructor(var remoteDao: AppRemoteDao, var filter: String) :
    PagingSource<Int, Episode>() {
    override fun getRefreshKey(state: PagingState<Int, Episode>): Int? {
        return state.anchorPosition
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Episode> {
        return try {
            val nextPage: Int = params.key ?: Constants.FIRST_PAGE_INDEX
            val response = remoteDao.getFilterEpisodes(query = nextPage, filter = filter )
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
                data = response.episodes,
                prevKey = null,
                nextKey = nextPageNumber
            )

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}