package com.example.foodnote.ui.diary_item_detail_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodnote.R
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.data.model.food.TotalFoodResult
import com.example.foodnote.databinding.FragmentDiaryItemDetailBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.diary_item_detail_fragment.adapter.DiaryItemProductAdapter
import com.example.foodnote.ui.diary_item_detail_fragment.adapter.ItemClickListener
import com.example.foodnote.ui.diary_item_detail_fragment.viewModel.DiaryItemDetailViewModel
import com.example.foodnote.utils.ViewComposeProgressBar
import com.example.foodnote.utils.hide
import com.example.foodnote.utils.show
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
        viewModel.getStateLiveData().observe(viewLifecycleOwner) { appState: AppState<*> ->
            setState(appState)
        }
        if (idUser.isEmpty()) {
            uiScope.launch {
                getUserId()
            }
        } else {
            viewModel.saveDiaryItem(args.diaryItem)
            args.diaryItem.dbId?.let { dbId ->
                viewModel.getSavedFoodCollection(idUser, dbId)
            }
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onStart() {
        initComposeProgressBar()
        super.onStart()
    }

    private suspend fun getUserId() {
        viewModel.getUserId().collect {
            idUser = it
            viewModel.saveDiaryItem(args.diaryItem)
            args.diaryItem.dbId?.let { dbId ->
                viewModel.getSavedFoodCollection(idUser, dbId)
            }
        }
    }

    private fun setState(appState: AppState<*>) {
        when (appState) {
            is AppState.Error -> {
                context?.showToast(appState.error?.message)
            }
            is AppState.Loading -> {
                if (appState.visible) {
                    viewModel.showProgressBar()
                    binding.productRcView.hide()
                }
            }
            is AppState.Success -> {
                when (val item = appState.data) {
                    is List<*> -> {
                        viewModel.hideProgressBar()
                        when (item.firstOrNull()) {
                            is FoodDto -> {
                                binding.productRcView.show()
                                initRcView(item as List<FoodDto>)
                            }
                        }
                    }
                    is TotalFoodResult -> {
                        viewModel.saveTotalCalculate(item)
                        setTotalView(item)
                    }
                    else -> viewModel.hideProgressBar()
                }
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
            timeItemTextView.text = args.diaryItem.time
            titleTextView.text = args.diaryItem.name
    }

    private fun initRcView(list: List<FoodDto>) {
        if (binding.productRcView.adapter == null) {
            binding.productRcView.layoutManager = LinearLayoutManager(context)
            binding.productRcView.adapter = adapter
            binding.productRcView.itemAnimator?.changeDuration = 0
        }
        adapter.setItem(list)
    }

    override fun addProduct(item: FoodDto, pos: Int) {
        item.incCount()
        viewModel.saveFood(item)
        adapter.notifyItemChanged(pos)
    }

    private fun initComposeProgressBar() {
        binding.progressBar.setContent {
            val isVisible by viewModel.getComposeLiveData().observeAsState()
            if (isVisible == true) {
                context?.ViewComposeProgressBar()
            }
        }
    }

    override fun deleteProduct(item: FoodDto, pos: Int) {
        item.decCount()
        viewModel.saveFood(item)
        adapter.notifyItemChanged(pos)
    }

    private companion object {
        const val LOG_TAG = "LOG_DIARY_ITEM_FRAGMENT"
    }
}