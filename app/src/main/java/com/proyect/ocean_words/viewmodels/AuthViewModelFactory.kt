package com.proyect.ocean_words.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proyect.ocean_words.core.security.JwtManager
import com.proyect.ocean_words.core.security.SecurePreferences


class AuthViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {

            val securePrefs = SecurePreferences(context)
            val jwtManager = JwtManager(securePrefs)

            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(jwtManager) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
