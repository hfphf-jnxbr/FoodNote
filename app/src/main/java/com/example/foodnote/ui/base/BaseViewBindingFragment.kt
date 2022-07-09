package com.example.foodnote.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

abstract class BaseViewBindingFragment<VB : ViewBinding>(
    private val inflate: (LayoutInflater, ViewGroup?, Boolean) -> VB
) : Fragment() {
    private var _binding: VB? = null
    val binding
        get() = _binding
            ?: throw IllegalStateException("Trying to access binding")

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }
    protected val idUser by lazy {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            currentUser.email ?: currentUser.uid
        } else {
            ""
        }
    }
    protected val uiScope by lazy {
        CoroutineScope(Dispatchers.Main)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
