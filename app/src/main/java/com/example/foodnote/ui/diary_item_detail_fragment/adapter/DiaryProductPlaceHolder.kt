package com.example.foodnote.ui.diary_item_detail_fragment.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.foodnote.R
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.databinding.DiaryProductItemBinding

class DiaryProductPlaceHolder(item: View) : RecyclerView.ViewHolder(item) {
    private val binding = DiaryProductItemBinding.bind(item)
    private val context = binding.root.context
    fun bind(item: FoodDto, itemClickListener: ItemClickListener) = with(binding) {
        Glide
            .with(context)
            .load(item.image)
            .apply(RequestOptions.circleCropTransform())
            .apply(
                RequestOptions()
                    .error(R.drawable.ic_baseline_fastfood_24)
            )
            .into(productImageView)

        productNameTextView.text = item.name
        countProductTextView.text = context.getString(R.string.count_products, item.count)
        protainCountTextView.text = context.getString(R.string.count_protein, item.protein)
        fatsCountTextView.text = context.getString(R.string.count_fats, item.fat)
        carbohydratesCountTextView.text =
            context.getString(R.string.count_carbohydrates, item.carbohydrate)
        if (item.count > 0) {
            countProductTextView.text = context.getString(R.string.count_products, item.count)
        }
        addImageButton.setOnClickListener {
            itemClickListener.addProduct(item, adapterPosition)
        }

        deleteImageButton.setOnClickListener {
            itemClickListener.deleteProduct(item, adapterPosition)
        }
    }
}