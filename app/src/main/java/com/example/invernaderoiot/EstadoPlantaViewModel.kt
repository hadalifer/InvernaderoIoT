package com.example.invernaderoiot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class EstadoPlantaViewModel(
    private val repository: InvernaderoRepository = InvernaderoRepository()
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<EstadoPlantaUiState>(EstadoPlantaUiState.Loading)
    val uiState: StateFlow<EstadoPlantaUiState> = _uiState.asStateFlow()

    init {
        cargarEstado()
    }

    fun cargarEstado() {
        _uiState.value = EstadoPlantaUiState.Loading
        viewModelScope.launch {
            try {
                val estado = repository.obtenerEstadoPlanta()
                _uiState.value = EstadoPlantaUiState.Success(estado)
            } catch (e: Exception) {
                _uiState.value = EstadoPlantaUiState.Error(
                    e.message ?: "Error desconocido"
                )
            }
        }
    }
}

/**
 * Convierte un timestamp ISO de AWS tipo:
 *  2025-12-08T16:15:06.569294Z
 * a algo más bonito:
 *  08 dic 2025, 10:15
 */
fun formatTimestamp(iso: String?): String {
    if (iso.isNullOrBlank()) return "Sin datos"

    return try {
        // Formato de entrada (el que manda tu Lambda)
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC")

        // Parseamos a Date
        val date: Date = inputFormat.parse(iso)

        // Formato de salida (bonito en español)
        val outputFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale("es", "MX"))

        outputFormat.format(date)
    } catch (e: Exception) {
        // Si algo falla, mostramos original
        iso
    }
}


