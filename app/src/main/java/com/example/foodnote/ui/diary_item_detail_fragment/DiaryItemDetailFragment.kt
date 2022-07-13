package com.example.foodnote.ui.diary_item_detail_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodnote.R
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.food.TotalFoodResult
import com.example.foodnote.databinding.FragmentDiaryItemDetailBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.diary_item_detail_fragment.adapter.DiaryItemProductAdapter
import com.example.foodnote.ui.diary_item_detail_fragment.adapter.ItemClickListener
import com.example.foodnote.ui.diary_item_detail_fragment.viewModel.DiaryItemDetailViewModel
import com.example.foodnote.utils.showToast
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class DiaryItemDetailFragment :
    BaseViewBindingFragment<FragmentDiaryItemDetailBinding>(FragmentDiaryItemDetailBinding::inflate),
    ItemClickListener {
    private val viewModel: DiaryItemDetailViewModel by viewModel()
    private val adapter by lazy {
        DiaryItemProductAdapter(this)
    }
    private val args: DiaryItemDetailFragmentArgs by navArgs()

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

    private suspend fun getUserId() {
        viewModel.getUserId().collect {
            idUser = it
        }
    }

    private fun setState(appState: SampleState) {

        uiScope.launch {
            if (appState.foodDtoItems.isNotEmpty()) {
                initRcView(appState.foodDtoItems)
            }

            appState.totalFoodResult?.let {
                setTotalView(it)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun setTotalView(total: TotalFoodResult) = with(binding) {
        countCalorieTextView.text = context?.getString(R.string.count_calorie, total.calorieSum)
        countFatTextView.text = context?.getString(R.string.count_fats, total.fatSum)
        countProteinTextView.text = context?.getString(R.string.count_protein, total.proteinSum)
        countCarbohydrateTextView.text =
            context?.getString(R.string.count_carbohydrates, total.carbohydrateSum)
    }

    private fun initView() = with(binding) {
        searchBar.searchTextInput.setOnEditorActionListener { v, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val searchWord = v.text.toString()
                viewModel.searchFood(searchWord)
                return@setOnEditorActionListener true
            }
            false
        }
        initTotalContainer()
    }

    private fun initTotalContainer() = with(binding) {
        uiScope.launch {
            timeItemTextView.text = args.diaryItem.time
            titleTextView.text = args.diaryItem.name
            args.diaryItem.dbId?.let { dbId ->
                viewModel.getSavedFoodCollection(idUser, dbId).collect { state ->
                    when (state) {
                        is AppState.Error -> {
                            context?.showToast(state.error?.message)
                        }
                        is AppState.Loading -> {
                            context?.showToast("Saved")
                        }
                        is AppState.Success -> {
                            initRcView(state.data)
                            viewModel.calculateTotalData(state.data)
                        }
                    }
                }
            }
        }
    }

    private fun initRcView(list: List<FoodDto>) {
        if (binding.productRcView.adapter == null) {
            binding.productRcView.layoutManager = LinearLayoutManager(context)
            binding.productRcView.adapter = adapter
            binding.productRcView.itemAnimator?.changeDuration = 0
        }
        adapter.setItem(list)
    }

    override fun addProduct(item: FoodDto) {
        uiScope.launch {
            viewModel.saveFood(item).collect { state ->
                if (state is AppState.Success) {
                    context?.showToast(state.data)
                }
            }
        }
    }

    private companion object {
        const val LOG_TAG = "LOG_DIARY_ITEM_FRAGMENT"
    }
}