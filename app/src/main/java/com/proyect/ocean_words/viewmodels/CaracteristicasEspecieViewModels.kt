package com.proyect.ocean_words.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.FirebaseFirestore
import com.proyect.ocean_words.model.EspecieEstado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class CaracteristicasEspecieViewModels : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _especie = MutableStateFlow<EspecieEstado?>(null)
    val especie: StateFlow<EspecieEstado?> = _especie

    fun getEspeciePorId(especieId: String) {
        viewModelScope.launch {
            try {
                val document = firestore.collection("especies").document(especieId).get().await()
                if (document.exists()) {
                    val especieData = document.toObject(EspecieEstado::class.java)
                    _especie.value = especieData
                    Log.i("Firebase", "Especie obtenida: ${especieData?.nombre}")
                } else {
                    Log.w("Firebase", "No se encontró el documento con ID: $especieId")
                    _especie.value = null
                }
            } catch (e: Exception) {
                Log.e("Firebase", "Error obteniendo especie: ", e)
                _especie.value = null
            }
        }
    }
}
