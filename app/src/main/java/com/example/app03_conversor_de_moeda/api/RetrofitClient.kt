package com.example.app03_conversor_de_moeda.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    val apiService: AwesomeApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://economia.awesomeapi.com.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AwesomeApiService::class.java)
    }
}
