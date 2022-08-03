package com.example.foodnote.ui.auth_fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.foodnote.R
import com.example.foodnote.data.base.SampleState
import com.example.foodnote.databinding.FragmentAuthBinding
import com.example.foodnote.ui.auth_fragment.viewModel.AuthViewModel
import com.example.foodnote.ui.base.BaseViewBindingFragment
import com.example.foodnote.utils.showToast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class AuthFragment : BaseViewBindingFragment<FragmentAuthBinding>(FragmentAuthBinding::inflate) {
    private val viewModel: AuthViewModel by viewModel()

    // Receiver
    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
            if (it.resultCode == RC_SIGN_IN) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(it.data)
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)
                    Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                    account.idToken?.let { token ->
                        firebaseAuthWithGoogle(token)
                    }
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w(TAG, "Google sign in failed", e)
                }
            }
        }

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val googleSignInClient: GoogleSignInClient by lazy {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context!!, gso)
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
        if (appState.isSuccess) {
            navigateToMainScreen()
        }

        if (appState.error != null) {
            context?.showToast(appState.error.message)
        }
    }

    private fun navigateToMainScreen() {
        val navController = findNavController()
        val action = AuthFragmentDirections.actionAuthFragmentToSettingsFragment()
        navController.navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.signInButton.setOnClickListener {
            signIn()
        }
        binding.authAnonimButton.setOnClickListener {
            val idToken = UUID.randomUUID().toString()
            viewModel.saveUserId(idToken)
        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        currentUser?.let {
            navigateToMainScreen()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        getResult.launch(signInIntent)
    }


    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    navigateToMainScreen()
                }
            }
    }

    private companion object {
        const val TAG = "GoogleActivity"
        const val RC_SIGN_IN = 9001
    }
}