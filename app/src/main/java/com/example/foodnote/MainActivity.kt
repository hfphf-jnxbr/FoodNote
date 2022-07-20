package com.example.foodnote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.foodnote.databinding.ActivityMainBinding
import com.example.foodnote.ui.base.viewModel.MainViewModel
import com.example.foodnote.utils.hide
import com.example.foodnote.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModel()
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initNavigation()

    }

    private fun initNavigation() {
        val navView = binding.bottomNavigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_root) as NavHostFragment
        val navController = navHostFragment.navController

        navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.splashScreenFragment, R.id.authFragment -> {
                    binding.bottomNavigation.hide()
                }
                else -> binding.bottomNavigation.show()
            }
        }
    }

}

