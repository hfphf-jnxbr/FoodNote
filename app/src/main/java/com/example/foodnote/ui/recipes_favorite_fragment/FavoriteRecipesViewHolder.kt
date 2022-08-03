package com.example.foodnote.ui.recipes_favorite_fragment

import androidx.core.view.isVisible
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
        var isVisibleIngr = false
        binding.textViewNameRecipes.text = item.label
        glide.load(item.image).apply(RequestOptions.bitmapTransform ( RoundedCorners (20))).into(binding.imageViewRecipes)
        val sb = StringBuilder()
        repeat(10) {
            sb.append("● $it\n")
        }
        binding.textViewIngrRecipes.text = sb.trim()

        binding.cardViewRecipes.setOnClickListener {
          isVisibleIngr = !isVisibleIngr
            binding.textViewIngrRecipes.isVisible = isVisibleIngr
        //    listener.onClickRecipes(item)
        }
    }
}