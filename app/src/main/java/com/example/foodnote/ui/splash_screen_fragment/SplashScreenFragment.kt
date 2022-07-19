package com.example.foodnote.ui.splash_screen_fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.foodnote.R
import com.example.foodnote.databinding.SplashScreenFragmentBinding
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.ui.splash_screen_fragment.viewModel.SplashScreenViewModel
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment :
    BaseViewBindingFragment<SplashScreenFragmentBinding>(SplashScreenFragmentBinding::inflate) {
    private val viewModel: SplashScreenViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.myLooper()!!).postDelayed({
            if (idUser.isNotEmpty()) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_notesFragment)
            } else {
                uiScope.launch {
                    getUserId()
                }
            }
        }, 6000)

    }

    private suspend fun getUserId() {
        viewModel.getUserId().collect { state ->
            if (state.isNotEmpty()) {
                findNavController().navigate(R.id.action_splashScreenFragment_to_notesFragment)
            } else {
                findNavController().navigate(R.id.action_splashScreenFragment_to_authFragment)
            }
        }
    }

}