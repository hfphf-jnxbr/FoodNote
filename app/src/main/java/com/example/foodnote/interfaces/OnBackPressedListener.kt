package com.example.foodnote.interfaces

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * Обработчик нажатия на системную клавишу "назад" во [Fragment].
 */
interface OnBackPressedListener {
    /**
     * Обрабатывает нажатие на кнопку "назад" в [Fragment].
     *
     * @return true, если [Fragment] обработал нажатие и false,
     * если нажатие должна обработать [Activity].
     */
    fun onBackPressed(): Boolean
}
