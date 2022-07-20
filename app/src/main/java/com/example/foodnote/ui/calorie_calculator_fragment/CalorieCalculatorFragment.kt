package com.example.foodnote.ui.calorie_calculator_fragment


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TimePicker
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodnote.R
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.DiaryItem
import com.example.foodnote.data.model.food.TotalFoodResult
import com.example.foodnote.databinding.FragmentCalorieCalculatorBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.base.viewModel.MainViewModel
import com.example.foodnote.ui.calorie_calculator_fragment.adapter.rc_view_adapter.CalorieCalculatorAdapter
import com.example.foodnote.ui.calorie_calculator_fragment.adapter.rc_view_adapter.ItemClickListener
import com.example.foodnote.ui.calorie_calculator_fragment.adapter.view_pager_adapter.TotalViewAdapter
import com.example.foodnote.ui.calorie_calculator_fragment.const.FragmentIndex
import com.example.foodnote.ui.calorie_calculator_fragment.viewModel.CalorieCalculatorViewModel
import com.example.foodnote.utils.showToast
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
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
    private val mainViewModel: MainViewModel by sharedViewModel()

    private val adapter by lazy {
        CalorieCalculatorAdapter(this)
    }
    private var totalViewAdapter: TotalViewAdapter? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getStateLiveData().observe(viewLifecycleOwner) { appState: AppState<*> ->
            setState(appState)
        }
        mainViewModel.getStateLiveData().observe(viewLifecycleOwner) { String ->

        }
        if (idUser.isEmpty()) {
            uiScope.launch {
                getUserId()
            }
        } else {
            initStartData()
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        totalViewAdapter = TotalViewAdapter(this)
        initView()
    }

    private fun initView() {
        initDate()
        initPager()
    }

    private suspend fun getUserId() {
        viewModel.getUserId().collect {
            idUser = it
            initStartData()
        }
    }

    private fun initStartData() {
        viewModel.getDiary(idUser)
        binding.addDiaryButton.setOnClickListener {
            showDialog { time, name ->
                val diaryItem = viewModel.generateRandomItem(idUser, time, name)
                viewModel.saveDiary(diaryItem)
            }
        }
    }

    private fun initPager() {
        viewModel.initCalorie()
        binding.pager.adapter = totalViewAdapter
        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            when (position) {
                FragmentIndex.CIRCLE_FRAGMENT.value.first -> {
                    tab.text = context?.getString(FragmentIndex.CIRCLE_FRAGMENT.value.second)
                }
                FragmentIndex.WATER_FRAGMENT.value.first -> {
                    tab.text = context?.getString(FragmentIndex.WATER_FRAGMENT.value.second)
                }
            }
        }.attach()
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
            val total = TotalFoodResult(
                calorieSum = kotlin.random.Random.nextDouble(300.0, 3000.0),
                calorieSumMax = kotlin.random.Random.nextDouble(1500.0, 3000.0),
                proteinSumMax = protein.second.toDouble(),
                proteinSum = protein.first.toDouble(),
                fatSum = fat.first.toDouble(),
                fatSumMax = fat.second.toDouble(),
                carbohydrateSumMax = 0.0,
                carbohydrateSum = 0.0
            )
            mainViewModel.initCircle(total)
        }


    private fun setState(state: AppState<*>) {
        when (state) {
            is AppState.Error -> context?.showToast(state.error?.message)
            is AppState.Loading -> context?.showToast("LOADING")
            is AppState.Success -> {
                when (val item = state.data) {
                    is List<*> -> {
                        when (item.firstOrNull()) {
                            is DiaryItem -> {
                                initRcView(state.data as MutableList<DiaryItem>)
                            }
                        }
                    }
                    is Triple<*, *, *> -> {
                        item as Triple<Pair<Int, Int>, Pair<Int, Int>, Pair<Int, Int>>
                        initCalories(item.first, item.second, item.third)
                    }
                }
            }
        }
    }

    private fun initRcView(list: MutableList<DiaryItem>) {
        if (binding.diaryContainerRcView.adapter == null) {
            binding.diaryContainerRcView.layoutManager = LinearLayoutManager(context)
            binding.diaryContainerRcView.adapter = adapter
            binding.diaryContainerRcView.itemAnimator?.changeDuration = 0
        }
        adapter.setItems(list)
    }

    private fun showDialog(callback: (time: String, name: String) -> Unit) {
        val builder = AlertDialog.Builder(context)
        // Set the dialog title
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.diary_create_dialog, null)
        val editText = view.findViewById<EditText>(R.id.name_diary_title_text_view)
        val timePicker = view.findViewById<TimePicker>(R.id.time_diary_time_picker)
        builder
            .setView(view)
            .setTitle(R.string.create_notes)
            .setPositiveButton(
                R.string.save
            ) { _, _ ->
                val text = editText.text.toString()
                val time = String.format("%02d:%02d", timePicker.hour, timePicker.minute)
                callback(time, text)
            }
            .setNegativeButton(
                R.string.disabled
            ) { dialog, _ ->
                dialog.cancel()
            }

        builder.create()
        builder.show()
    }

    override fun navigateToDiaryDetail(item: DiaryItem) {
        val navController = findNavController()
        val action = CalorieCalculatorFragmentDirections
            .actionCalorieCalculatorFragmentToDiaryItemDetailFragment(item)
        navController.navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        totalViewAdapter = null
    }
}