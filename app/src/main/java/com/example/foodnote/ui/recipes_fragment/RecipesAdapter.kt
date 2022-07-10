package com.example.foodnote.ui.recipes_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.example.foodnote.databinding.ItemRecipesBinding

class RecipesAdapter(private var glide: RequestManager,private val listener: RecipesListener) : ListAdapter<Recipes, RecipesViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recipes>() {
            override fun areItemsTheSame(oldItem: Recipes, newItem: Recipes) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Recipes, newItem: Recipes) =
                oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        RecipesViewHolder(listener,
            ItemRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false),glide
        )

    override fun onBindViewHolder(holder: RecipesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

interface RecipesListener{
    fun onClickRecipes(recipes: Recipes)
    fun onClickRecipesLike(recipes: Recipes)
}