package com.example.invernaderoiot

import com.google.gson.Gson

class InvernaderoRepository(
    private val api: InvernaderoApi = ApiClient.api,
    private val gson: Gson = Gson()
) {
    suspend fun obtenerEstadoPlanta(): EstadoPlantaResponse {
        val lambdaResponse = api.obtenerEstado()

        if (lambdaResponse.statusCode != 200) {
            throw Exception("Error en Lambda: statusCode=${lambdaResponse.statusCode}")
        }

        // body es un JSON en string → lo convertimos a EstadoPlantaResponse
        return gson.fromJson(lambdaResponse.body, EstadoPlantaResponse::class.java)
    }
}

