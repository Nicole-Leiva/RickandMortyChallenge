package com.example.android.networkconnect.presentation.bindingadapter

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import coil.load
import com.example.android.networkconnect.R

class CharacterRowBinding {
    companion object{
        @BindingAdapter("loadImageFromUrl")
        @JvmStatic
        fun loadImageFromUrl(imageView: ImageView, imageUrl: String){
            imageView.load(imageUrl){
                crossfade(600)
            }
        }

        @BindingAdapter("changeColorStatus")
        @JvmStatic
        fun changeColorStatus(view: View, status: String){
            when(status){
                "Alive" ->{view.setBackgroundColor(ContextCompat.getColor(view.context, R.color.green))}
                "Dead" ->{view.setBackgroundColor(ContextCompat.getColor(view.context,R.color.red))}
                "unknown" ->{view.setBackgroundColor(ContextCompat.getColor(view.context,R.color.gray))}
            }
        }
    }
}