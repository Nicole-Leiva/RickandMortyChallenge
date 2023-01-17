package com.example.android.networkconnect.presentation.episode

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.example.android.networkconnect.R
import com.example.android.networkconnect.databinding.FragmentEpisodesBinding
import com.example.android.networkconnect.presentation.adapter.EpisodeAdapter
import com.example.android.networkconnect.support.uimodel.EpisodeUIModel
import com.example.android.networkconnect.utils.actionFragment
import com.example.android.networkconnect.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class EpisodesFragment : Fragment(R.layout.fragment_episodes) {
    private val binding by viewBinding(FragmentEpisodesBinding::bind)
    private val viewModel: EpisodeViewModel by viewModels()
    private lateinit var mAdapter: EpisodeAdapter
    private lateinit var selectedSeason: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setRecyclerAdapter()

        viewModel.getEpisodes()
        episodeListCollect()
        setDropdownArrayAdapter()
    }

    private fun setRecyclerAdapter() {
        mAdapter = EpisodeAdapter(object : EpisodeItemClickListener {
            override fun onItemClickListener(episodeUIModel: EpisodeUIModel) {
                Navigation.actionFragment(
                    requireView(), EpisodesFragmentDirections.actionEpisodesFragmentToEpisodeDetailFragment(episodeUIModel)
                )
            }
        })
        binding.episodeAdapter = mAdapter
    }

    private fun episodeListCollect() {
        lifecycleScope.launch {
            viewModel.episodeList.collectLatest {
                mAdapter.submitData(it)

            }
        }
    }

    private fun setDropdownArrayAdapter() {
        viewModel.setHashMapSeasonAndEpisode()
        //create array adapter
        var episodeArrayAdapter: ArrayAdapter<String>
        val seasonArrayAdapter = ArrayAdapter(
            requireContext(),
            R.layout.dropdown_season_item,
            viewModel.seasonListHashMap
        )

        binding.autoCompleteSeason.setText(R.string.select_season)
        binding.autoCompleteEpisode.setText(R.string.select_episode)
        binding.autoCompleteSeason.setAdapter(seasonArrayAdapter)

        binding.autoCompleteSeason.setOnItemClickListener { _, _, i, _ ->
            binding.autoCompleteEpisode.setText(R.string.select_episode)
            selectedSeason = seasonArrayAdapter.getItem(i).toString()
            if (viewModel.episodeListHashMap.contains("Episode 11")) {
                viewModel.episodeListHashMap.remove("Episode 11")
            }
            episodeArrayAdapter = if (seasonArrayAdapter.getItem(i) == "Season 1") {
                viewModel.episodeListHashMap.add("Episode 11")
                ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_episode_item,
                    viewModel.episodeListHashMap
                )
            } else {
                ArrayAdapter(
                    requireContext(),
                    R.layout.dropdown_episode_item,
                    viewModel.episodeListHashMap
                )
            }
            binding.autoCompleteEpisode.setAdapter(episodeArrayAdapter)
            selectedEpisode(episodeArrayAdapter)
        }
    }

    //setDropdownArrayAdapter içinde çağırılıyor
    private fun selectedEpisode(episodeArrayAdapter: ArrayAdapter<String>) {
        var seasonCode = ""
        var episodeCode = ""
        viewModel.seasonHashMap.forEach {
            if (selectedSeason == it.value) {
                seasonCode = it.key
                viewModel.getFilterEpisodes(seasonCode)
            }
        }

        binding.autoCompleteEpisode.setOnItemClickListener { adapterView, view, i, l ->
            viewModel.episodeHashMap.forEach {
                if (episodeArrayAdapter.getItem(i) == it.value) {
                    episodeCode = it.key //örn: E03
                    viewModel.getFilterEpisodes("$seasonCode$episodeCode")
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setDropdownArrayAdapter()
    }
}