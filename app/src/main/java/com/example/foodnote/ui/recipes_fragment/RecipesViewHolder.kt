package com.example.foodnote.ui.recipes_fragment

import androidx.recyclerview.widget.RecyclerView
import com.example.foodnote.databinding.ItemRecipesBinding

class RecipesViewHolder(
    private val binding: ItemRecipesBinding,
): RecyclerView.ViewHolder(binding.root){
    fun bind(item: Recipes) {
        binding.textViewNameRecipes.text = item.nameRecipes
    }
}