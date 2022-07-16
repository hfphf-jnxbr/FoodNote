package com.example.foodnote.ui.settings_fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.foodnote.R
import com.example.foodnote.data.base.AppState
import com.example.foodnote.data.interactor.settings_interactor.SettingColumnRequire
import com.example.foodnote.databinding.FragmentSettingsBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.settings_fragment.viewModel.SettingsViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment :
    BaseViewBindingFragment<FragmentSettingsBinding>(FragmentSettingsBinding::inflate) {
    private val viewModel: SettingsViewModel by viewModel()
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
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setState(appState: AppState<*>) = with(binding) {
        when (appState) {

            is AppState.Error -> {

            }
            is AppState.Loading -> {

            }
            is AppState.Success -> {
                when (val item = appState.data) {
                    is Pair<*, *> -> {
                        item as Pair<SettingColumnRequire, Boolean>
                        when (item.first) {
                            SettingColumnRequire.MODE -> {
                                if (!item.second) {
                                    modeTextInputLayout.error = getString(R.string.select_meta)
                                } else {
                                    modeTextInputLayout.error = null
                                }
                            }
                            SettingColumnRequire.WEIGHT -> {
                                if (!item.second) {
                                    weightTextInput.error = getString(R.string.select_weight)
                                }
                            }
                            SettingColumnRequire.HEIGHT -> {
                                if (!item.second) {
                                    heightTextInput.error = getString(R.string.select_height)
                                }
                            }
                            SettingColumnRequire.SEX -> {
                                if (!item.second) {
                                    binding.femaleRadioButton.error = getString(R.string.select_sex)
                                    binding.maleRadioButton.error = getString(R.string.select_sex)
                                } else {
                                    binding.femaleRadioButton.error = null
                                    binding.maleRadioButton.error = null
                                }

                            }
                        }
                    }
                }
            }

            else -> {}
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        initSelectedAdapter()
        binding.saveButton.setOnClickListener {
            val height = binding.heightTextInput.text.toString()
            val weight = binding.weightTextInput.text.toString()
            val types = binding.modeAutoCompleteTextView.text.toString()
            val isFemale = binding.femaleRadioButton.isChecked
            val isMale = binding.maleRadioButton.isChecked
            viewModel.checkRequireColumn(types, weight, height, isMale, isFemale)
        }
    }

    private suspend fun getUserId() {
        viewModel.getUserId().collect {
            idUser = it
        }
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