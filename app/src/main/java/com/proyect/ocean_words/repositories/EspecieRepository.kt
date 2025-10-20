package com.proyect.ocean_words.repositories

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.proyect.ocean_words.model.EspecieEstado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await


class EspecieRepository {

    private val database : DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("especies")
    private val _especies= MutableStateFlow<List<EspecieEstado>>(emptyList())
    val especies : StateFlow<List<EspecieEstado>> = _especies
    private val _especie = MutableStateFlow(EspecieEstado())
    val especie : StateFlow<EspecieEstado> = _especie
    fun buscarEspeciePorId(claveBusqueda : String) {
        database.child(claveBusqueda).addValueEventListener(object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    try {
                        val especieEncontrada = snapshot.getValue(EspecieEstado::class.java)

                        especieEncontrada?.let {
                            _especie.value = it
                        }

                    } catch (e: Exception) {
                        println("Error al mapear los datos de Especie: ${e.message}")
                    }
                } else {
                    println("No se encontró la especie con clave: $claveBusqueda")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al leer movimientos: ${error.message}")
            }
        })
    }
}