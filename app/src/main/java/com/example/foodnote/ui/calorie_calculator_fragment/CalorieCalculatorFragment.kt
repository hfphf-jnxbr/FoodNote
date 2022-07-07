package com.example.foodnote.ui.calorie_calculator_fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodnote.R
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.databinding.FragmentCalorieCalculatorBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.calorie_calculator_fragment.adapter.CalorieCalculatorAdapter
import com.example.foodnote.ui.calorie_calculator_fragment.adapter.ItemClickListener
import com.example.foodnote.ui.calorie_calculator_fragment.viewModel.CalorieCalculatorViewModel
import com.example.foodnote.utils.hide
import com.example.foodnote.utils.show
import com.example.foodnote.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*


class CalorieCalculatorFragment :
    BaseViewBindingFragment<FragmentCalorieCalculatorBinding>(FragmentCalorieCalculatorBinding::inflate),
    ItemClickListener {
    private val viewModel: CalorieCalculatorViewModel by viewModel()
    private val adapter by lazy {
        CalorieCalculatorAdapter(this)
    }

    private val uiScope by lazy {
        CoroutineScope(Dispatchers.Main)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getStateLiveData().observe(viewLifecycleOwner) { appState: SampleState ->
            setState(appState)
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initDate()
        initCircle()
        viewModel.initCalorie()
        uiScope.launch {
            getDiary()
        }
        binding.addDiaryButton.setOnClickListener {
            val diaryItem = viewModel.generateRandomItem()
            viewModel.saveDiary(diaryItem)
        }
    }

    private fun initCircle() = with(binding) {
        circleDiagramView.start(1499, 1500, 100, 150, 30, 70)
    }

    private fun initDate() = with(binding) {
        val current = LocalDateTime.now()
        val date: String = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            .withLocale(Locale("ru"))
            .format(LocalDate.of(current.year, current.month, current.dayOfMonth))
        dayWeekMaterialTextView.text = date
    }

    private fun initCalories(protein: Pair<Int, Int>, fat: Pair<Int, Int>, carb: Pair<Int, Int>) =
        with(binding) {
            protainValueTextView.text =
                resources.getString(R.string.format_challenge, protein.first, protein.second)
            fatsValueTextView.text =
                resources.getString(R.string.format_challenge, fat.first, fat.second)
            carbohydratesValueTextView.text =
                resources.getString(R.string.format_challenge, carb.first, carb.second)
        }

    private suspend fun getDiary() {
        viewModel.getDiary().collect { state ->
            when (state) {
                is AppState.Loading -> {
                    binding.root.context.showToast("LOADING")
                    binding.diaryCardView.hide()
                }

                is AppState.Success -> {
                    binding.diaryCardView.show()
                    initRcView(state.data as MutableList<DiaryItem>)
                }

                is AppState.Error -> {
                    binding.diaryCardView.hide()
                }
                else -> {}
            }
        }
    }

    private fun setState(state: SampleState) {

        if (state.calorie != null) {
            val calorie = state.calorie
            initCalories(calorie.first, calorie.second, calorie.third)
        }
    }

    private fun initRcView(list: MutableList<DiaryItem>) {
        if (binding.diaryContainerRcView.adapter == null) {
            binding.diaryContainerRcView.layoutManager = LinearLayoutManager(context)
            binding.diaryContainerRcView.adapter = adapter
            binding.diaryContainerRcView.itemAnimator?.changeDuration = 0
        }
        adapter.setItem(list)
    }
}