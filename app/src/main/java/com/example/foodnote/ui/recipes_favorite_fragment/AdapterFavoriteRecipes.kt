package com.example.foodnote.ui.recipes_favorite_fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.RequestManager
import com.example.foodnote.data.databaseRoom.entities.EntitiesRecipes
import com.example.foodnote.databinding.ItemFavoriteRecipesBinding

class AdapterFavoriteRecipes(private var glide: RequestManager) : ListAdapter<EntitiesRecipes, FavoriteRecipesViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<EntitiesRecipes>() {
            override fun areItemsTheSame(oldItem: EntitiesRecipes, newItem: EntitiesRecipes) =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: EntitiesRecipes, newItem: EntitiesRecipes) =
                oldItem == newItem
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)=
        FavoriteRecipesViewHolder(
            ItemFavoriteRecipesBinding.inflate(LayoutInflater.from(parent.context), parent, false),glide
        )

    override fun onBindViewHolder(holder: FavoriteRecipesViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}