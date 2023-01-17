package com.example.android.networkconnect.presentation.character

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.android.networkconnect.R
import com.example.android.networkconnect.databinding.FragmentCharactersBinding
import com.example.android.networkconnect.presentation.adapter.CharacterAdapter
import com.example.android.networkconnect.utils.actionFragment
import com.example.android.networkconnect.utils.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters), SearchView.OnQueryTextListener {
    private val binding by viewBinding(FragmentCharactersBinding::bind)
    private val viewModel: CharacterViewModel by viewModels()
    private val navArgs: CharactersFragmentArgs by navArgs()
    private val mAdapter by lazy { CharacterAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        viewModel.backFromBottomSheet.value = navArgs.backFromBottomSheet
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).setSupportActionBar(binding.toolbarCharacter)
        binding.characterAdapter = mAdapter

        setupNavArgs()
        swipeRefresh()
        observe()
        eventCollect()
        characterListCollect()

    }

    private fun eventCollect() = lifecycleScope.launch {
        viewModel.characterLoading.collectLatest {
            handleProgressBar(it)
        }
    }

    private fun handleProgressBar(isLoading : Boolean) {
        binding.progressBarCharacter.isVisible = isLoading
        binding.rvCharacter.isVisible = !isLoading
    }

    private fun characterListCollect() {
        lifecycleScope.launch {
            viewModel.characterList.collectLatest { characterList ->
                mAdapter.submitData(characterList)
            }
        }
    }

    private fun observe() {
        viewModel.backFromBottomSheet.observe(viewLifecycleOwner) { back ->
            if (!back) {
                viewModel.getCharacters()
            } else {
                viewModel.getFilterCharacters()
            }
        }
    }

    private fun swipeRefresh() {
        binding.swipeCharacterFragment.setOnRefreshListener {
            viewModel.backFromBottomSheet.value = false
            binding.swipeCharacterFragment.isRefreshing = false
        }
    }

    private fun setupNavArgs() {
        navArgs.status?.let { viewModel.changeValueCharacterStatus(it) }
        navArgs.species?.let { viewModel.changeValueCharacterSpecies(it) }
        navArgs.gender?.let { viewModel.changeValueCharacterGender(it) }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_character_toolbar, menu)
        val item = menu.findItem(R.id.action_search)
        val searchView = item.actionView as SearchView
        searchView.setOnQueryTextListener(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_filter -> {
                Navigation.actionFragment(
                    requireView(),
                    R.id.action_charactersFragment_to_characterFilterBottomSheet
                )
            }
            R.id.action_refresh -> {
                viewModel.backFromBottomSheet.value = false
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String): Boolean {
        if (newText.isBlank()) {
            observe()
        } else {
            viewModel.searchCharacterName(newText)
        }
        return true
    }
}