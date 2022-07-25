package com.example.foodnote.ui.recipes_fragment

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.foodnote.data.model.recipes.Recipes
import com.example.foodnote.databinding.FragmentRecipesBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import org.koin.androidx.viewmodel.ext.android.viewModel


class RecipesFragment :
    BaseViewBindingFragment<FragmentRecipesBinding>(FragmentRecipesBinding::inflate) {

    private val viewModel: RecipesViewModel by viewModel()
    private val adapterRecipes: RecipesAdapter by lazy { RecipesAdapter(Glide.with(requireContext())) }
    private var behavior: BottomSheetBehavior<*>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBehavior()
        initAdapter()
        initListener()
        initObservers()
    }

    private fun initObservers() {
        viewModel.listRecipes.observe(viewLifecycleOwner) { list ->
            if (list.hints.isNotEmpty()) { adapterRecipes.submitList(list.hints) }
        }
    }

    private fun initListener() {
        binding.searchViewRecipes.setOnQueryTextListener (object :SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText?.trim() != null)
                viewModel.searchRecipesByIngr(newText.trim())
                return false
            }

        })
    }

    private fun initAdapter() {
        binding.recyclerViewRecipes.adapter = adapterRecipes
        adapterRecipes.setListenerRecipes(object : RecipesListener {
            override fun onClickRecipes(recipes: Recipes) {
                behavior!!.state = BottomSheetBehavior.STATE_EXPANDED
                Glide.with(requireContext()).load(recipes.images.REGULAR.url)
                    .into(binding.includeRecipesDetails.imageViewRecipes)

                binding.includeRecipesDetails.textViewNameRecipes.text = recipes.label
                val sb = StringBuilder()
                recipes.ingredientLines.forEach {
                    if (it != "") sb.append("● $it\n")
                }
                binding.includeRecipesDetails.textViewDescriptionRecipes.text =sb.trim()

            }

            override fun onClickRecipesLike(recipes: Recipes) {
                //TODO добавить в БД
                Toast.makeText(requireContext(),
                    "В избранное добавлен рецепт ${recipes.label}",
                    Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun initBehavior() {
        behavior = BottomSheetBehavior.from(binding.includeRecipesDetails.bottomSheet)
        behavior!!.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {}
            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }
}
