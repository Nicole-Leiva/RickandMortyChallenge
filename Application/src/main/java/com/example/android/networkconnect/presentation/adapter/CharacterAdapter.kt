package com.example.android.networkconnect.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.AutoTransition
import androidx.transition.TransitionManager
import com.example.android.networkconnect.R
import com.example.android.networkconnect.databinding.CharacterRowLayoutBinding
import com.example.android.networkconnect.support.uimodel.CharacterUIModel
import com.example.android.networkconnect.utils.changeHeight
import com.example.android.networkconnect.utils.hide
import com.example.android.networkconnect.utils.show

class CharacterAdapter() :
    PagingDataAdapter<CharacterUIModel, CharacterAdapter.CharacterViewHolder>(DiffUtilCallBack()) {
    class CharacterViewHolder(var binding: CharacterRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(character: CharacterUIModel) {
            binding.character = character
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharacterViewHolder {
        val view: CharacterRowLayoutBinding =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.character_row_layout, parent, false)
        return CharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        val currentCharacter = getItem(position)!!
        holder.bind(currentCharacter)
        val hb = holder.binding
        val mContext = hb.root.context

        hb.expandableLayout.hide()
        hb.viewBlackAlpha.show()
        hb.ivRowCharacterImage.changeHeight(mContext,160)


        hb.cardViewRowCharacter.setOnClickListener {
            if (hb.expandableLayout.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(hb.cardViewRowCharacter, AutoTransition())
                hb.expandableLayout.show()
                hb.viewBlackAlpha.hide()
                hb.ivRowCharacterImage.changeHeight(mContext,270)
            } else {
                TransitionManager.endTransitions(hb.cardViewRowCharacter)
                hb.expandableLayout.hide()
                hb.viewBlackAlpha.show()
                hb.ivRowCharacterImage.changeHeight(mContext,160)
            }
        }
    }

    class DiffUtilCallBack : DiffUtil.ItemCallback<CharacterUIModel>() {
        override fun areItemsTheSame(
            oldItem: CharacterUIModel,
            newItem: CharacterUIModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CharacterUIModel,
            newItem: CharacterUIModel
        ): Boolean {
            return oldItem == newItem
        }

    }
}