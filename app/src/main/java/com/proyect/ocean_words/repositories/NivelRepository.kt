package com.proyect.ocean_words.repositories

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.proyect.ocean_words.model.NivelEstado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class NivelRepository {
    private val database : DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("niveles")
    private val _especies = MutableStateFlow<List<NivelEstado>>(emptyList())
    val especies : StateFlow<List<NivelEstado>> = _especies
    private val _especie = MutableStateFlow(NivelEstado())
    val especie :StateFlow<NivelEstado> = _especie
    fun mostrarNiveles(){
        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val especiesList = snapshot.children.mapNotNull { child ->
                    child.getValue(NivelEstado::class.java)
                }
                _especies.value = especiesList

            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Firebase", "Error al leer movimientos: ${error.message}")
            }
        })
    }
}