package com.proyect.ocean_words.viewmodels

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.proyect.ocean_words.model.EspecieEstado
import com.proyect.ocean_words.model.SlotEstado
import com.proyect.ocean_words.repositories.EspecieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlin.text.lowercase

class EspecieViewModel (
    animal: String,
    dificultad: String,
    private val onPerderVidaGlobal: () -> Unit
) : ViewModel() {
    private val repository = EspecieRepository()
    private val MAX_LIVES = 3
    private val RECHARGE_COOLDOWN_MS = 1 * 60 * 1000L
    private val _especie = MutableStateFlow<EspecieEstado?>(null)
    val especie = _especie.asStateFlow()

    val animalSinEspacios: String = animal.trim().lowercase().replace(" ", "")
    private val tamanoTeclado: Int
    private val _navegarAExito = MutableLiveData<Boolean>()
    val navegarAExito: LiveData<Boolean> = _navegarAExito
    private val letrasPorFila = 7

    private val _pistaUsada = MutableStateFlow(false)
    val pistaUsada: StateFlow<Boolean> = _pistaUsada.asStateFlow()
    private val _mostrarMensajePistaUsada = MutableLiveData<Boolean>()
    val mostrarMensajePistaUsada: LiveData<Boolean> = _mostrarMensajePistaUsada

    /*private val _vidas = MutableStateFlow(listOf(true, true, true))
    val vidas = _vidas.asStateFlow()

    private val _lastLifeLossTime = MutableStateFlow<Long?>(null)

    val timeToNextLife: StateFlow<String> = flow {
        while(true) {
            val lastLoss = _lastLifeLossTime.value
            if (lastLoss != null) {
                val timeElapsed = System.currentTimeMillis() - lastLoss
                val timeLeft = RECHARGE_COOLDOWN_MS - timeElapsed

                if (timeLeft > 0) {
                    emit(formatTime(timeLeft))
                } else {
                    regenerateOneLifeAndCheckRestart()
                }
            } else {
                emit("")
            }
            delay(1000)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "")*/

    private fun formatTime(ms: Long): String {
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }

    fun perderVida() {
        onPerderVidaGlobal()
    }

    val animalRandom: String = if (dificultad != "dificil") {
        shuffleText(animalSinEspacios)
    } else {
        val letrasRandom = getTwoRandomLetters()
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
            _navegarAExito.value = true

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
                    indicesIncorrectos.add(i)
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
        val quedanVidas = true

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
        if (_pistaUsada.value) {
            _mostrarMensajePistaUsada.value = true
            return
        }
        // 1️⃣ Encuentra todos los índices donde la letra aún no está colocada
        val indicesVacios = respuestaJugador.mapIndexedNotNull { index, slot ->
            if (slot?.char == null) index else null
        }
        if (indicesVacios.isEmpty()) return
        // 2️⃣ Elegir un índice vacío aleatorio (para que la pista sea aleatoria)
        val indexPista = indicesVacios.random()

        // 3️⃣ Colocar la letra correcta en ese índice
        val letraCorrecta = animalSinEspacios[indexPista]
        respuestaJugador[indexPista] = SlotEstado(char = letraCorrecta, esCorrecto = true)
        _pistaUsada.value = true
    }
    fun mensajePistaMostrado() {
        _mostrarMensajePistaUsada.value = false
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
//    fun mostrarEspeciePorId(idEspecie: String) {
//        viewModelScope.launch {
//            repository.buscarEspeciePorId(idEspecie)
//                .catch {
//                    _especie.value = null
//                }
//                .collect { especieEncontrada ->
//                    _especie.value = especieEncontrada
//                }
//        }
//    }
}