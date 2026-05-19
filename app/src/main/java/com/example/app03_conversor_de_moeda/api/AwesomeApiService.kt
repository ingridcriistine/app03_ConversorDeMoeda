// api/AwesomeApiService.kt

package com.example.app03_conversor_de_moeda.api

import com.example.app03_conversor_de_moeda.model.CurrencyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface AwesomeApiService {

    @GET("{moedas}")
    suspend fun getCotacao(

        @Path("moedas")
        moedas: String

    ): Response<Map<String, CurrencyResponse>>
}