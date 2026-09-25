package com.example.invernaderoiot

import retrofit2.http.GET
import retrofit2.http.Query

interface InvernaderoApi {

    @GET("lecturas")
    suspend fun obtenerEstado(
        @Query("dispositivoId") dispositivoId: String = "invernadero-01"
    ): LambdaEstadoResponse
}

