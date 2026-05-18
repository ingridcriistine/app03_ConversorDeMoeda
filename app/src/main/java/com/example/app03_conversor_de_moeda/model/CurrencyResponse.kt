package com.example.app03_conversor_de_moeda.model

data class CurrencyResponse(
    val USDBRL: ExchangeData?=null,
    val BRLUSD: ExchangeData?=null,
    val BTCBRL: ExchangeData?=null,
    val BRLBTC: ExchangeData?=null,
    val BTCUSD: ExchangeData?=null,
    val USDBTC: ExchangeData?=null
) {
    data class ExchangeData(
        val bid:String,
        val ask:String
    )
}