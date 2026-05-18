package com.example.app03_conversor_de_moeda.api

import com.example.app03_conversor_de_moeda.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AwesomeApiService {
    @GET("json/last/{pair}")
    suspend fun getRate(@Path("pair") pair: String): CurrencyResponse
}
