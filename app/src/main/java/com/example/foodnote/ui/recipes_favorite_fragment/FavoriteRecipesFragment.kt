package com.example.foodnote.ui.recipes_favorite_fragment


import android.os.Bundle
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.example.foodnote.data.databaseRoom.entities.EntitiesRecipes
import com.example.foodnote.databinding.FragmentFavoriteRecipesBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.recipes_favorite_fragment.viewModel.FavoriteRecipesViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class FavoriteRecipesFragment :
    BaseViewBindingFragment<FragmentFavoriteRecipesBinding>(FragmentFavoriteRecipesBinding::inflate) {
    private val viewModel: FavoriteRecipesViewModel by viewModel()
    private val adapter: AdapterFavoriteRecipes by lazy {
        AdapterFavoriteRecipes(Glide.with(requireContext()))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerViewFavoriteRecipes.adapter = adapter
        viewModel.listFavoriteRecipes.observe(viewLifecycleOwner) {
            Log.i("youTag", "${it.size}")
            if (it.isNotEmpty()) adapter.submitList(it)
            else {
                //TODO пустой список
            }
        }
        adapter.setListenerRecipes(object : FavoriteRecipesListener {
            override fun onClickRecipes(recipes: EntitiesRecipes) {
                //TODO раскрывать список
             }
        })
    }
}