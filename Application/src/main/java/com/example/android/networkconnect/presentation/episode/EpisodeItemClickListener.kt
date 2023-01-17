package com.example.android.networkconnect.presentation.episode

import com.example.android.networkconnect.support.uimodel.EpisodeUIModel


interface EpisodeItemClickListener {
    fun onItemClickListener(episodeUIModel: EpisodeUIModel)
}