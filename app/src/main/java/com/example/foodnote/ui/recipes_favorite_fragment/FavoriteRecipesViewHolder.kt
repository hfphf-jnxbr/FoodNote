package com.example.foodnote.ui.recipes_favorite_fragment

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.foodnote.data.databaseRoom.entities.EntitiesRecipes
import com.example.foodnote.databinding.ItemFavoriteRecipesBinding
import com.google.gson.Gson

class FavoriteRecipesViewHolder(
    private val binding: ItemFavoriteRecipesBinding,
    private var glide: RequestManager,
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(item: EntitiesRecipes) {
        var isVisibleIngr = false
        binding.textViewNameRecipes.text = item.label
        glide.load(item.image).apply(RequestOptions.bitmapTransform(RoundedCorners(20)))
            .into(binding.imageViewRecipes)
        val ingredientLines = Gson().fromJson(item.ingredientLines, Array<String>::class.java)
        val sb = StringBuilder()
        ingredientLines.forEach {
            sb.append("● ${it}\n")
        }
        binding.textViewIngrRecipes.text = sb.trim()

        binding.cardViewRecipes.setOnClickListener {
            isVisibleIngr = !isVisibleIngr
            binding.textViewIngrRecipes.isVisible = isVisibleIngr
        }
    }
}