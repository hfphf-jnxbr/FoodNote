package com.example.foodnote.ui.recipes_fragment

import android.os.Bundle
import android.view.View
import com.example.foodnote.databinding.FragmentRecipesBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment


class RecipesFragment : BaseViewBindingFragment<FragmentRecipesBinding>(FragmentRecipesBinding::inflate) {

    private lateinit var adapterRecipes:RecipesAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapterRecipes = RecipesAdapter()

        binding.recyclerViewRecipes.adapter = adapterRecipes

        val list = listOf<Recipes>(Recipes("","fsdfsdfsd","fsdfsdfsdfsdfsdf"),
            Recipes("","fsdfsdfsd","fsdfsdfsdfsdfsdf"),
            Recipes("","fsdfsdfsd","fsdfsdfsdfsdfsdf"),
            Recipes("","fsdfsdfsd","fsdfsdfsdfsdfsdf"),
            Recipes("","fsdfsdfsd","fsdfsdfsdfsdfsdf"),
            Recipes("","fsdfsdfsd","fsdfsdfsdfsdfsdf"),
            Recipes("","fsdfsdfsd","fsdfsdfsdfsdfsdf"))

        adapterRecipes.submitList(list)

    }
}