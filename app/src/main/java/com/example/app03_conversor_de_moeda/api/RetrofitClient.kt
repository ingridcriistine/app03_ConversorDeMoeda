// api/RetrofitClient.kt

package com.example.app03_conversor_de_moeda.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL =
        "https://economia.awesomeapi.com.br/json/last/"

    val api: AwesomeApiService by lazy {

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .build()
            .create(AwesomeApiService::class.java)
    }
}