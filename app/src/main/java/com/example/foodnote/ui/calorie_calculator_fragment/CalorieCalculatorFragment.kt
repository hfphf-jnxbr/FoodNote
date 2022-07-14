package com.example.foodnote.ui.calorie_calculator_fragment


import android.app.AlertDialog
import android.content.DialogInterface
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


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getStateLiveData().observe(viewLifecycleOwner) { appState: SampleState ->
            setState(appState)
        }
        if (idUser.isEmpty()) {
            uiScope.launch {
                getUserId()
            }
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
            showDialog { time, name ->
                val diaryItem = viewModel.generateRandomItem(idUser, time, name)
                viewModel.saveDiary(diaryItem)
            }
        }
    }

    private suspend fun getUserId() {
        viewModel.getUserId().collect {
            idUser = it
        }
    }

    private fun initCircle() = with(binding) {
        circleDiagramView.start(1420, 1500, 100, 150, 24, 70)
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
        viewModel.getDiary(idUser).collect { state ->
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

    private fun showDialog(callback: (time: String, name: String) -> Unit) {
        val builder = AlertDialog.Builder(context)
        // Set the dialog title
        val inflater = requireActivity().layoutInflater;
        val view = inflater.inflate(R.layout.diary_create_dialog, null)
        val editText = view.findViewById<EditText>(R.id.name_diary_title_text_view)
        val timePicker = view.findViewById<TimePicker>(R.id.time_diary_time_picker)
        builder
            .setView(view)
            .setTitle(R.string.create_notes)
            .setPositiveButton(R.string.save,
                DialogInterface.OnClickListener { _, _ ->
                    val text = editText.text.toString()
                    val time = "${timePicker.hour}:${timePicker.minute}"
                    callback(time, text)
                })
            .setNegativeButton(R.string.disabled,
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })

        builder.create()
        builder.show()
    }

    override fun navigateToDiaryDetail(item: DiaryItem) {
        val navController = findNavController()
        val action = CalorieCalculatorFragmentDirections
            .actionCalorieCalculatorFragmentToDiaryItemDetailFragment(item)
        navController.navigate(action)
    }


}