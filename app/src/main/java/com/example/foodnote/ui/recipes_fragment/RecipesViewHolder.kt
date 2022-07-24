package com.example.foodnote.ui.recipes_fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.foodnote.data.model.recipes.Recipes
import com.example.foodnote.data.model.recipes.RecipesX
import com.example.foodnote.databinding.ItemRecipesBinding

class RecipesViewHolder(
    private val listener: RecipesListener,
    private val binding: ItemRecipesBinding,
    private var glide: RequestManager
): RecyclerView.ViewHolder(binding.root){
    fun bind(item: RecipesX) {
        binding.textViewNameRecipes.text = item.recipe.label
        glide.load(item.recipe.images.THUMBNAIL.url).into(binding.imageViewRecipes)
        binding.cardViewRecipes.setOnClickListener { listener.onClickRecipes(item.recipe) }
        binding.imageViewRecipesLike.setOnClickListener { listener.onClickRecipesLike(item.recipe) }
    }
}