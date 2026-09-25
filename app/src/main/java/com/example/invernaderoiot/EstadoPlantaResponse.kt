package com.example.invernaderoiot

data class EstadoPlantaResponse(
    val ok: Boolean,
    val dispositivoId: String,
    val timestamp: String,
    val humedad: Double,
    val temperatura: Double,
    val hayLuz: Boolean,
    val estadoHumedad: String,
    val mensajeHumedad: String,
    val estadoLuz: String,
    val mensajeLuz: String
)

