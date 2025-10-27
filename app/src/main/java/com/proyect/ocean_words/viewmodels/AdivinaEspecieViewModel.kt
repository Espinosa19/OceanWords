package com.proyect.ocean_words.viewmodels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.proyect.ocean_words.model.SlotEstado
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AdivinaEspecieViewModel (
    animal: String,
    dificultad: String
) : ViewModel() {
    val animalSinEspacios: String = animal.trim().replace(" ", "")
    private val tamanoTeclado: Int
    private val _navegarAExito = MutableLiveData<Boolean>()
    val navegarAExito: LiveData<Boolean> = _navegarAExito
    private val letrasPorFila = 7
    private val _vidas = MutableStateFlow(listOf(true, true, true))
    val vidas = _vidas.asStateFlow()

    // Función para perder una vida (de derecha a izquierda)
    fun perderVida() {
        val index = _vidas.value.indexOfLast { it } // encuentra la última vida activa
        if (index != -1) {
            val nuevasVidas = _vidas.value.toMutableList()
            nuevasVidas[index] = false
            _vidas.value = nuevasVidas
        }
    }

    // Función para reiniciar todas las vidas
    fun reiniciarVidas() {
        _vidas.value = listOf(true, true, true)
    }
    val animalRandom: String = if (dificultad != "dificil") {
            shuffleText(animalSinEspacios)
        } else {
            val letrasRandom = getTwoRandomLetters() // Implementa estas funciones en el VM o inyectalas
            shuffleText(animalSinEspacios + letrasRandom.joinToString(""))
        }
        init {
            tamanoTeclado = animalRandom.length
        }

        val visible = mutableStateListOf<Boolean>().apply {
            repeat(tamanoTeclado) { add(true) }
        }

        val respuestaJugador = mutableStateListOf<SlotEstado?>().apply {
            repeat(animalSinEspacios.length) { add(SlotEstado()) }
        }

        val botonADondeFue = mutableStateListOf<Int>().apply {
            repeat(tamanoTeclado) { add(-1) }
        }

    private fun validarPalabraCompleta() {

        val respuestaCompleta = respuestaJugador.map { it?.char ?: ' ' }.joinToString("")
        val esPalabraCorrecta = respuestaCompleta.equals(animalSinEspacios, ignoreCase = true)

        if (esPalabraCorrecta) {
            for (i in respuestaJugador.indices) {
                val currentChar = respuestaJugador[i]?.char
                respuestaJugador[i] = SlotEstado(char = currentChar, esCorrecto = true)
            }
            _navegarAExito.value = true // O un objeto de evento más específico

        } else {
            perderVida()
            val indicesIncorrectos = mutableListOf<Int>()
            for (i in respuestaJugador.indices) {
                val letraJugador = respuestaJugador[i]?.char
                val letraCorrecta = animalSinEspacios[i]

                val esLetraCorrectaEnPosicion = letraJugador != null &&
                        letraJugador.equals(letraCorrecta, ignoreCase = true)

                if (esLetraCorrectaEnPosicion) {
                    respuestaJugador[i] = SlotEstado(char = letraJugador, esCorrecto = true)
                } else {
                    respuestaJugador[i] = SlotEstado(char = letraJugador, esCorrecto = false)
                    indicesIncorrectos.add(i) // Guardar el índice para la remoción
                }
            }

            removerLetrasIncorrectas(indicesIncorrectos)
        }
    }
    fun navegacionAExitoCompleta() {
        _navegarAExito.value = false
    }
    /**
     * Remueve las letras de las posiciones indicadas de respuestaJugador,
     * y restablece la visibilidad de los botones correspondientes.
     * * @param indicesARemover Una lista de los índices en respuestaJugador que son incorrectos.
     */
    private fun removerLetrasIncorrectas(indicesARemover: List<Int>) {
        for (posicionEnRespuesta in indicesARemover) {
            if (respuestaJugador[posicionEnRespuesta] != null) {

                val indiceOriginalBoton = botonADondeFue.indexOfFirst { it == posicionEnRespuesta }

                if (indiceOriginalBoton != -1) {
                    visible[indiceOriginalBoton] = true

                    botonADondeFue[indiceOriginalBoton] = -1 // Usar -1 o un valor que indique "no asignado"
                }

                // 4. Quitar la letra de la respuesta, dejando el slot nulo.
                respuestaJugador[posicionEnRespuesta] = null
            }
        }
    }
        fun selectLetter(char: Char, originalIndex: Int) {
            val quedanVidas = _vidas.value.any { it }

            val posicionAsignada = respuestaJugador.indexOfFirst { it?.char == null }
            if(quedanVidas){

                if (posicionAsignada != -1) {
                    respuestaJugador[posicionAsignada] = SlotEstado(char = char, esCorrecto = null)
                    visible[originalIndex] = false
                    botonADondeFue[originalIndex] = posicionAsignada

                    val slotsVaciosRestantes = respuestaJugador.count { it?.char == null }

                    if (slotsVaciosRestantes == 0) {
                        validarPalabraCompleta()
                    }
                }
            }
            else{

            }
        }

        fun removeLetter(responseSlotIndex: Int) {
            respuestaJugador[responseSlotIndex] = SlotEstado(char = null, esCorrecto = null)

            val originalButtonIndex = botonADondeFue.indexOf(responseSlotIndex)
            if (originalButtonIndex != -1) {
                botonADondeFue[originalButtonIndex] = -1
                visible[originalButtonIndex] = true
            }
        }

        fun goBackGame() {
            var slotVaciado = -1

            for (i in respuestaJugador.indices.reversed()){
                if (respuestaJugador[i]?.char != null) {
                    respuestaJugador[i] = SlotEstado(char = null, esCorrecto = null)
                    slotVaciado = i
                    break
                }
            }

            if (slotVaciado != -1) {
                val originalButtonIndex = botonADondeFue.indexOf(slotVaciado)
                if (originalButtonIndex != -1) {
                    botonADondeFue[originalButtonIndex] = -1
                    visible[originalButtonIndex] = true
                }
            }
        }
    fun obtenerPista() {
        // 1️⃣ Encuentra todos los índices donde la letra aún no está colocada
        val indicesVacios = respuestaJugador.mapIndexedNotNull { index, slot ->
            if (slot?.char == null) index else null
        }

        if (indicesVacios.isEmpty()) return // ya está completa

        // 2️⃣ Elegir un índice vacío aleatorio (para que la pista sea aleatoria)
        val indexPista = indicesVacios.random()

        // 3️⃣ Colocar la letra correcta en ese índice
        val letraCorrecta = animalSinEspacios[indexPista]
        respuestaJugador[indexPista] = SlotEstado(char = letraCorrecta, esCorrecto = true)
    }


    fun resetGame() {
            visible.indices.forEach { i -> visible[i] = true }
            botonADondeFue.indices.forEach { i -> botonADondeFue[i] = -1 }
            respuestaJugador.indices.forEach { i ->
                respuestaJugador[i] = SlotEstado(char = null, esCorrecto = null)
            }
        }

        private fun getTwoRandomLetters(): List<Char> {
            val abecedario =('a'..'z')
            return List(2) { abecedario.random() } }
        private fun shuffleText(text: String): String {
            return text.toList().shuffled().joinToString("")
        }


}