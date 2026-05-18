package com.example.app03_conversor_de_moeda.api

import com.example.app03_conversor_de_moeda.model.CurrencyResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface AwesomeAPI {
    @GET("json/last/{from}-{to}")
    suspend fun getRate(
        @Path("from") from:String,
        @Path("to") to:String
    ): CurrencyResponse
}