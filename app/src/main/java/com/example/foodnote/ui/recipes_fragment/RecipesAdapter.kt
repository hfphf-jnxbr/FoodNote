package com.example.foodnote.ui.recipes_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.example.foodnote.data.model.recipes.Recipes
import com.example.foodnote.data.model.recipes.RecipesX
import com.example.foodnote.databinding.ItemRecipesBinding

class RecipesAdapter(private var glide: RequestManager) : ListAdapter<RecipesX, RecipesViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<RecipesX>() {
            override fun areItemsTheSame(oldItem: RecipesX, newItem: RecipesX) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: RecipesX, newItem: RecipesX) =
                oldItem == newItem
        }
    }
    lateinit var listener: RecipesListener
    fun setListenerRecipes(listener: RecipesListener){
        this.listener = listener
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