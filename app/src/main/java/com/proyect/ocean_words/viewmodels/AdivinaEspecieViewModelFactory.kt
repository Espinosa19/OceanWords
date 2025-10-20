package com.proyect.ocean_words.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AdivinaEspecieViewModelFactory (
        private val animal: String,
        private val dificultad: String
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EspecieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EspecieViewModel(animal, dificultad) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
