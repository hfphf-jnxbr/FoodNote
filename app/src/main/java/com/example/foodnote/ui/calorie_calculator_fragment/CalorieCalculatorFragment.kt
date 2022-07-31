package com.example.foodnote.ui.calorie_calculator_fragment


import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TimePicker
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
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
import com.example.foodnote.ui.calorie_calculator_fragment.viewModel.CalorieCalculatorViewModel
import com.example.foodnote.utils.showToast
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
        mainViewModel.getStateLiveData().observe(viewLifecycleOwner) { str ->
            Log.d("tag", str.toString())
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
                val diaryItem = viewModel.generateItem(idUser, time, name)
                viewModel.saveDiary(diaryItem)
            }
        }
    }

    private fun initPager() = with(binding) {
        pager.adapter = totalViewAdapter

        diagrams.elevation = 20f
        buttonRight.setOnClickListener {
            if(pager.currentItem != 1) {
                pager.setCurrentItem(1,true)
                customTextView.setText("Water")
            }
        }

        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if(position == 0) {
                    customTextView.setText("Calories")
                } else {
                    customTextView.setText("Water")
                }
            }
        })

        buttonLeft.setOnClickListener {
            if(pager.currentItem != 0) {
                pager.setCurrentItem(0, true)
                customTextView.setText("Calories")
            }
        }
    }



    private fun initDate() = with(binding) {
        val current = LocalDateTime.now()
        val date: String = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
            .withLocale(Locale("ru"))
            .format(LocalDate.of(current.year, current.month, current.dayOfMonth))
        dayWeekMaterialTextView.text = date
    }

    private fun initCalories(total: TotalFoodResult) =
        with(binding) {
            protainValueTextView.text =
                resources.getString(
                    R.string.format_challenge,
                    total.proteinSum,
                    total.proteinSumMax
                )
            fatsValueTextView.text =
                resources.getString(R.string.format_challenge, total.fatSum, total.fatSumMax)
            carbohydratesValueTextView.text =
                resources.getString(
                    R.string.format_challenge,
                    total.carbohydrateSum,
                    total.carbohydrateSumMax
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
                    is TotalFoodResult -> {
                        initCalories(item)
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
                val time =
                    resources.getString(R.string.format_time, timePicker.hour, timePicker.minute)
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