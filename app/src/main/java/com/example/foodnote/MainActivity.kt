package com.example.foodnote

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.MenuRes
import androidx.annotation.NavigationRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.foodnote.databinding.ActivityMainBinding
import com.example.foodnote.ui.base.interfaces.OnBackPressedListener
import com.example.foodnote.viewModels.MainViewModel

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewModel: MainViewModel by viewModels()
    private var navHostFragment: NavHostFragment? = null
    private lateinit var binding: ActivityMainBinding
    private lateinit var destinationChangeListener: NavController.OnDestinationChangedListener
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container_root) as NavHostFragment
        this.navHostFragment = navHostFragment
        val navController = navHostFragment.navController
        @MenuRes val menu: Int = R.menu.bottom_navigation_menu
        @NavigationRes val navigationGraph: Int = R.navigation.navigation_root
        destinationChangeListener = NavController.OnDestinationChangedListener { _, _, _ ->
            viewModel.selectedItemId = binding.bottomNavigation.selectedItemId
        }


        navController.graph = navController.navInflater.inflate(navigationGraph)
        binding.bottomNavigation.menu.clear()
        binding.bottomNavigation.inflateMenu(menu)
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemReselectedListener {}
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            NavigationUI.onNavDestinationSelected(item, navController)
            return@setOnItemSelectedListener true
        }
        val w = window
        w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        w.statusBarColor = ContextCompat.getColor(this, R.color.black)
        viewModel.selectedItemId = bundle?.getInt(KEY_SELECTED_ITEM)
    }

    companion object {
        private const val KEY_SELECTED_ITEM = "key_selected_item"
    }


    override fun onBackPressed() {
        // Получаем фрагмент, который в данный момент видит пользователь (согласно графу navigation_root.xml)
        val fragment = supportFragmentManager.primaryNavigationFragment
            ?.childFragmentManager?.primaryNavigationFragment
        // Пытаемся вызвать у фрагмента onBackPressed
        (fragment as? OnBackPressedListener)?.onBackPressed()?.let { if (it) return }
    }
}