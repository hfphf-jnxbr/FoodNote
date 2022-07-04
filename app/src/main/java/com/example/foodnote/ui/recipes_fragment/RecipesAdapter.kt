package com.example.foodnote.ui.recipes_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.foodnote.databinding.ItemRecipesBinding

class RecipesAdapter : ListAdapter<Recipes, RecipesViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recipes>() {
            override fun areItemsTheSame(oldItem: Recipes, newItem: Recipes) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Recipes, newItem: Recipes) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        RecipesViewHolder(
            ItemRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}