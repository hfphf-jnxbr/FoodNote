package com.example.foodnote.ui.calorie_calculator_fragment


import android.os.Bundle
import android.view.View
import com.example.foodnote.databinding.FragmentCalorieCalculatorBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class CalorieCalculatorFragment :
    BaseViewBindingFragment<FragmentCalorieCalculatorBinding>(FragmentCalorieCalculatorBinding::inflate) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDate()
    }

    override fun onResume() {
        super.onResume()
        initCircle()
    }

    private fun initCircle() = with(binding) {
        circleDiagramView.start(1222, 1500, 100, 150, 30, 70)
    }

    private fun initDate() = with(binding) {
        val current = LocalDateTime.now()
        val date: String = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            .withLocale(Locale("ru"))
            .format(LocalDate.of(current.year, current.month, current.dayOfMonth))
        dayWeekMaterialTextView.text = date
    }

}