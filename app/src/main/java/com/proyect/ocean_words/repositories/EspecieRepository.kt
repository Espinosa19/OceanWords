package com.proyect.ocean_words.repositories

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore


class EspecieRepository (
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
){
    private val especiesRef = firestore.collection("especies")



    fun buscarEspeciePorId(claveBusqueda : String) {

    }
}