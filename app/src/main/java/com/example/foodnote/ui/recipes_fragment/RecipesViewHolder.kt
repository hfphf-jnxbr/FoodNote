package com.example.foodnote.ui.recipes_fragment

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.foodnote.databinding.ItemRecipesBinding

class RecipesViewHolder(
    private val listener: RecipesListener,
    private val binding: ItemRecipesBinding,
    private var glide: RequestManager
): RecyclerView.ViewHolder(binding.root){
    fun bind(item: Recipes) {
        binding.textViewNameRecipes.text = item.nameRecipes
        glide.load(item.imageUrl).into(binding.imageViewRecipes)
        binding.cardViewRecipes.setOnClickListener { listener.onClickRecipes(item) }
        binding.imageViewRecipesLike.setOnClickListener { listener.onClickRecipesLike(item) }
    }
}