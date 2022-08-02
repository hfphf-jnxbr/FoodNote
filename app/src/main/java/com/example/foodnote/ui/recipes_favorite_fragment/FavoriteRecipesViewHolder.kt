package com.example.foodnote.ui.recipes_favorite_fragment

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.foodnote.data.databaseRoom.entities.EntitiesRecipes
import com.example.foodnote.databinding.ItemFavoriteRecipesBinding

class FavoriteRecipesViewHolder(
    private val listener: FavoriteRecipesListener,
    private val binding: ItemFavoriteRecipesBinding,
    private var glide: RequestManager
): RecyclerView.ViewHolder(binding.root){
    fun bind(item: EntitiesRecipes) {
        binding.textViewNameRecipes.text = item.label
        glide.load(item.image).apply(RequestOptions.bitmapTransform ( RoundedCorners (20))).into(binding.imageViewRecipes)
        binding.cardViewRecipes.setOnClickListener { listener.onClickRecipes(item) }
    }
}