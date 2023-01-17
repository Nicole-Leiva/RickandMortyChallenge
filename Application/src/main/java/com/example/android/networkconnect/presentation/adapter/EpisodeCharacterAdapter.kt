package com.example.android.networkconnect.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.android.networkconnect.R
import com.example.android.networkconnect.databinding.EpisodeCharacterRowLayoutBinding
import com.example.android.networkconnect.support.uimodel.CharacterUIModel

class EpisodeCharacterAdapter(var characterList: List<CharacterUIModel>) :
    RecyclerView.Adapter<EpisodeCharacterAdapter.EpisodeCharacterViewHolder>() {
    class EpisodeCharacterViewHolder(var binding: EpisodeCharacterRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(characterUIModel: CharacterUIModel){
            binding.character = characterUIModel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeCharacterViewHolder {
        val view: EpisodeCharacterRowLayoutBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.episode_character_row_layout, parent, false
        )
        return EpisodeCharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: EpisodeCharacterViewHolder, position: Int) {
        val currentCharacter = characterList[position]
        val hb = holder.binding
        val mContext = hb.root.context
        holder.bind(currentCharacter)
    }

    override fun getItemCount(): Int = characterList.size
}