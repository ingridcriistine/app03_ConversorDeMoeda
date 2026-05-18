package com.example.app03_conversor_de_moeda.model

import com.google.gson.annotations.SerializedName

data class CurrencyRate(
    @SerializedName("bid") val bid: String,
    @SerializedName("ask") val ask: String
)

typealias CurrencyResponse = Map<String, CurrencyRate>
