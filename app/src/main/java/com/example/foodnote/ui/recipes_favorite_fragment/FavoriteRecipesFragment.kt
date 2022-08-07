package com.example.foodnote.ui.recipes_favorite_fragment


import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import com.bumptech.glide.Glide
import com.example.foodnote.R
import com.example.foodnote.app.App
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.databaseRoom.entities.EntitiesRecipes
import com.example.foodnote.databinding.FragmentFavoriteRecipesBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.recipes_favorite_fragment.viewModel.FavoriteRecipesViewModel
import com.example.foodnote.utils.SwipeHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

        viewModel.getStateLiveData().observe(viewLifecycleOwner){
            when(it){
                is AppState.Error -> Unit
                is AppState.Loading -> {
                    binding.emptyList.visibility = View.GONE
                    binding.progress.visibility = View.VISIBLE
                    binding.recyclerViewFavoriteRecipes.visibility = View.GONE
                }
                is AppState.Success -> {
                    binding.progress.visibility = View.GONE
                    val list = it.data as List<EntitiesRecipes>
                    if (list.isNotEmpty()) {
                        binding.emptyList.visibility = View.GONE
                        binding.recyclerViewFavoriteRecipes.visibility = View.VISIBLE
                        adapter.submitList(list)
                    }
                    else {
                        binding.emptyList.visibility = View.VISIBLE
                        binding.recyclerViewFavoriteRecipes.visibility = View.GONE
                    }
                }
            }
        }
        val itemTouchHelper =
            ItemTouchHelper(object : SwipeHelper(requireActivity(), binding.recyclerViewFavoriteRecipes) {
                override fun addButtonsRight(position: Int): ButtonGroup {
                    val archiveButton = Button(
                        requireContext().applicationContext,
                        "Удалить",
                        14.0f,
                        R.color.md_theme_light_error,
                        16f,
                        Button.ButtonIcon(
                            requireContext().applicationContext,
                            R.drawable.ic_remove,
                            Button.ButtonIcon.IconLocation.START,
                            R.dimen.icon_size_swipe,
                            R.dimen.icon_size_swipe,
                            marginStart = 16,
                            marginEnd = 16
                        ),
                        object : ButtonClickListener {
                            override fun onClick() {
                                val recipes = adapter.currentList[position] ?: return
                                viewModel.removeRecipes(recipes)
                            }
                        }
                    )
                    return ButtonGroup(listOf(archiveButton), ButtonBinding.EdgeItemView)
                }

                override fun addButtonsLeft(position: Int) = ButtonGroup()
            })
        itemTouchHelper.attachToRecyclerView(binding.recyclerViewFavoriteRecipes)
    }
}