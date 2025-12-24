package com.proyect.ocean_words.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AdivinaEspecieViewModelFactory (
        private val animal: String,
        private val dificultad: String,
        private val usuariosViewModel: UsuariosViewModel
) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EspecieViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return EspecieViewModel(animal, dificultad, usuariosViewModel) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
