package com.example.foodnote.ui.calorie_calculator_fragment.sub_fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.food.TotalFoodResult
import com.example.foodnote.databinding.CircleLayoutBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.base.viewModel.MainViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class CircleFragment : BaseViewBindingFragment<CircleLayoutBinding>(CircleLayoutBinding::inflate) {
    private val mainViewModel: MainViewModel by sharedViewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainViewModel.getStateLiveData().observe(viewLifecycleOwner) {
            setState(it)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setState(appState: AppState<*>) {
        when (appState) {
            is AppState.Error -> {}
            is AppState.Loading -> {}
            is AppState.Success -> {
                when (appState.data) {
                    is TotalFoodResult -> {
                        val total = appState.data
                        binding.circleContainerDiagramView.start(
                            total.calorieSum.toInt(),
                            total.calorieSumMax.toInt(),
                            total.fatSum.toInt(),
                            total.fatSumMax.toInt(),
                            total.proteinSum.toInt(),
                            total.proteinSumMax.toInt()
                        )
                    }
                }
            }
        }
    }

}