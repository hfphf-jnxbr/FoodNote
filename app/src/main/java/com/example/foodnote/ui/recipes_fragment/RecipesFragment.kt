package com.example.foodnote.ui.recipes_fragment

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key.Companion.K
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.Glide
import com.example.foodnote.R
import com.example.foodnote.data.model.recipes.Recipes
import com.example.foodnote.databinding.FragmentRecipesBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.noteBook.stateData.StateDataDonat
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RecipesFragment :
    BaseViewBindingFragment<FragmentRecipesBinding>(FragmentRecipesBinding::inflate) {

    private val viewModel: RecipesViewModel by viewModel()
    private val adapterRecipes: RecipesAdapter by lazy { RecipesAdapter(Glide.with(requireContext())) }
    private var behavior: BottomSheetBehavior<*>? = null

    private val viewModelDonat : ViewModelDonatViewCompose by viewModel()
    private val scope = CoroutineScope(Dispatchers.Main)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBehavior()
        initAdapter()
        initListener()
        initObservers()

        initComposeView()
    }

    @Composable
    fun StateCompose() {
        var expanded by remember { mutableStateOf(false) }
        var angle  by remember { mutableStateOf(true) }

        viewModelDonat.getLiveData().observe(viewLifecycleOwner) { stateData ->
            when(stateData) {
                is StateDataDonat.SetData -> {
                    expanded = stateData.expanded
                    angle = stateData.angle
                }
                else -> {}
            }
        }

        scope.launch { delay(100)
            viewModelDonat.setStateDonat(expanded = false,angle = false)
            delay(2000)
            viewModelDonat.setStateDonat(expanded = true,angle = false)
            scope.cancel()
        }

        ViewCompose(expanded,angle)
    }

    @Composable
    fun AnimationCompose(angle: Boolean): Float {
        val value by animateFloatAsState(
            targetValue = if (angle) { 0f } else { 360f },
            animationSpec = tween(durationMillis = 2000, easing = LinearOutSlowInEasing)
        )
        return value
    }

    @Composable
    fun ViewCompose(expanded: Boolean, angle: Boolean) {
        val value = AnimationCompose(angle = angle)

        Card(
            modifier = Modifier.padding(all = 8.dp).clickable { viewModelDonat.setAnimated() },
            shape = RoundedCornerShape(80.dp),
            elevation = 10.dp,
            border = BorderStroke(2.dp, Color.LightGray)) {
            Column(
                Modifier.padding(all = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) { Image(
                    painter = painterResource(id = R.drawable.donat_anim),
                    contentDescription = "",
                    modifier = Modifier
                        .rotate(value)
                        .size(width = 200.dp, height = 200.dp))

                Spacer(modifier = Modifier.height(20.dp))
                AnimatedVisibility(visible = expanded) {
                    Text(text = "Search recipes", fontSize = 22.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 16.dp))
                }
            }
        }
    }

    private fun initComposeView() { binding.composeView.setContent { StateCompose() } }

    private fun initObservers() {
        viewModel.listRecipes.observe(viewLifecycleOwner) { list ->
            if (list.hints.isNotEmpty()) { adapterRecipes.submitList(list.hints) }
        }
        viewModel.successAddRecipes.observe(viewLifecycleOwner){
            if(it) Toast.makeText(requireContext(),"Рецепт успешно добавлен в избранное",Toast.LENGTH_LONG).show()
        }
    }

    private fun initListener() {
        binding.searchViewRecipes.isSubmitButtonEnabled = true
        binding.searchViewRecipes.setOnQueryTextListener (object :SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if(query?.trim() != null)
                viewModel.searchRecipesByIngr(query.trim())
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
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
                    .into(binding.includeRecipesDetails.imageRecipes)

                binding.includeRecipesDetails.textViewNameRecipes.text = recipes.label
                val sb = StringBuilder()
                recipes.ingredientLines.forEach {
                    if (it != "") sb.append("● $it\n")
                }
                binding.includeRecipesDetails.textViewDescriptionRecipes.text =sb.trim()

            }

            override fun onClickRecipesLike(recipes: Recipes) {
              viewModel.addRecipesInDatabase(recipes)
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
