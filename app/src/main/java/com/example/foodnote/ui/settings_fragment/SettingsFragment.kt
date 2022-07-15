package com.example.foodnote.ui.settings_fragment


import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import com.example.foodnote.R
import com.example.foodnote.databinding.FragmentSettingsBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment


class SettingsFragment :
    BaseViewBindingFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initSelectedAdapter()
    }

    private fun initSelectedAdapter() = with(binding) {
        val types = resources.getStringArray(R.array.type_modes)
        val adapter = ArrayAdapter(
            binding.root.context, R.layout.type_mode_item_layout, types
        )
        modeAutoCompleteTextView.setAdapter(adapter)
        modeAutoCompleteTextView.setOnClickListener { modeAutoCompleteTextView.showDropDown() }
    }

}