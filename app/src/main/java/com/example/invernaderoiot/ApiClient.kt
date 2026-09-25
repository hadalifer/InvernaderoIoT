package com.example.invernaderoiot

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL =
        "https://clr5up92zk.execute-api.us-east-1.amazonaws.com/prod/"

    val api: InvernaderoApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(InvernaderoApi::class.java)
    }
}

