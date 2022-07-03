package com.example.foodnote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.foodnote.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
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
    }

    companion object {
        private const val KEY_SELECTED_ITEM = "key_selected_item"
    }


}