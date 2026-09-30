package com.example.invernaderoiot

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = ""
      // Url de la Api, por seguridad la borre.

    val api: InvernaderoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InvernaderoApi::class.java)
    }
}

