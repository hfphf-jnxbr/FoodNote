package com.example.foodnote.ui.diary_item_detail_fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.data.model.food.FoodDto
import com.example.foodnote.databinding.FragmentDiaryItemDetailBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.diary_item_detail_fragment.adapter.DiaryItemProductAdapter
import com.example.foodnote.ui.diary_item_detail_fragment.adapter.ItemClickListener
import com.example.foodnote.ui.diary_item_detail_fragment.viewModel.DiaryItemDetailViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel


class DiaryItemDetailFragment :
    BaseViewBindingFragment<FragmentDiaryItemDetailBinding>(FragmentDiaryItemDetailBinding::inflate),
    ItemClickListener {
    private val viewModel: DiaryItemDetailViewModel by viewModel()
    private val adapter by lazy {
        DiaryItemProductAdapter(this)
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


    private fun setState(appState: SampleState) {
        if (appState.foodDtoItems.isNotEmpty()) {
            initRcView(appState.foodDtoItems)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
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
    }

    private fun initRcView(list: List<FoodDto>) {
        if (binding.productRcView.adapter == null) {
            binding.productRcView.layoutManager = LinearLayoutManager(context)
            binding.productRcView.adapter = adapter
            binding.productRcView.itemAnimator?.changeDuration = 0
        }
        adapter.setItem(list)
    }

    private companion object {
        const val LOG_TAG = "LOG_DIARY_ITEM_FRAGMENT"
    }
}