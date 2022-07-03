package com.example.foodnote

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.foodnote.ui.base.interfaces.OnBackPressedListener
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        val navController = findNavController(R.id.container)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        // Получаем фрагмент, который в данный момент видит пользователь (согласно графу navigation_root.xml)
        val fragment = supportFragmentManager.primaryNavigationFragment
            ?.childFragmentManager?.primaryNavigationFragment
        // Пытаемся вызвать у фрагмента onBackPressed
        (fragment as? OnBackPressedListener)?.onBackPressed()?.let { if (it) return }
    }
}