package com.proyect.ocean_words.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.proyect.ocean_words.viewmodels.AdivinaEspecieViewModel

class AdivinaEspecieViewModelFactory (
        private val animal: String,
        private val dificultad: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AdivinaEspecieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return AdivinaEspecieViewModel(animal, dificultad) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
