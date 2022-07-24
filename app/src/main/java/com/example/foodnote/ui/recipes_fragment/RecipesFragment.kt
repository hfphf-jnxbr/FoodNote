package com.example.foodnote.ui.recipes_fragment

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.foodnote.data.base.RetrofitRecipesImpl
import com.example.foodnote.data.datasource.recipes_datasource.RepositoryRecipesImpl
import com.example.foodnote.data.model.recipes.Recipes
import com.example.foodnote.data.model.recipes.RecipesList
import com.example.foodnote.databinding.FragmentRecipesBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior


class RecipesFragment :
    BaseViewBindingFragment<FragmentRecipesBinding>(FragmentRecipesBinding::inflate) {

    private lateinit var adapterRecipes: RecipesAdapter
    private var behavior: BottomSheetBehavior<*>? = null
    lateinit var viewModel: RecipesViewModel
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofitRecipesImpl = RetrofitRecipesImpl()
        val repository = RepositoryRecipesImpl(retrofitRecipesImpl)
        viewModel = RecipesViewModel(repository)

        behavior = BottomSheetBehavior.from(binding.includeRecipesDetails.bottomSheet)
        behavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        adapterRecipes = RecipesAdapter(Glide.with(requireContext()))

        binding.recyclerViewRecipes.adapter = adapterRecipes
        adapterRecipes.setListenerRecipes(object : RecipesListener {
            override fun onClickRecipes(recipes: Recipes) {

                behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                Glide.with(requireContext()).load(recipes.images.REGULAR.url)
                    .into(binding.includeRecipesDetails.imageViewRecipes)

                binding.includeRecipesDetails.textViewNameRecipes.text = recipes.label
                binding.includeRecipesDetails.textViewDescriptionRecipes.text =
                    recipes.ingredientLines.toString()

            }

            override fun onClickRecipesLike(recipes: Recipes) {
                Toast.makeText(requireContext(),
                    "В избранное добавлен рецепт ${recipes.label}",
                    Toast.LENGTH_LONG).show()
            }
        })

        binding.ddddd.setOnClickListener { viewModel.searchRecipesByIngr("chicken") }

        viewModel.listRecipes.observe(viewLifecycleOwner) { list ->
            Log.i("youTag","${list.hints}")
                if(list.hints.isNotEmpty()){
                    adapterRecipes.submitList(list.hints)
                    viewModel.listRecipes.removeObservers(viewLifecycleOwner)
                }
        }
    }
}
