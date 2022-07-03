package com.example.foodnote.ui.calorie_calculator_fragment


import android.os.Bundle
import android.view.View
import com.example.foodnote.databinding.FragmentCalorieCalculatorBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import java.time.LocalDateTime


class CalorieCalculatorFragment :
    BaseViewBindingFragment<FragmentCalorieCalculatorBinding>(FragmentCalorieCalculatorBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() = with(binding) {
        val date = LocalDateTime.now()
        dayWeekMaterialTextView.text = date.toString()
        circleDiagramView.start(1222, 1500, 100, 150, 30, 70)
    }

}