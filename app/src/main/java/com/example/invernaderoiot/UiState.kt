package com.example.invernaderoiot
sealed class EstadoPlantaUiState {
    object Loading : EstadoPlantaUiState()
    data class Success(val data: EstadoPlantaResponse) : EstadoPlantaUiState()
    data class Error(val message: String) : EstadoPlantaUiState()
}
