package com.example.app03_conversor_de_moeda.service

import com.example.app03_conversor_de_moeda.api.RetrofitClient

object CurrencyConverter {

    data class ResultadoConversao(

        val valorConvertido: Double,

        val cotacao: Double
    )

    suspend fun converter(

        origem: String,
        destino: String,
        valor: Double

    ): ResultadoConversao {

        // DIRETA

        val direta =
            buscarCotacao(origem, destino)

        if (direta != null) {

            return ResultadoConversao(

                valor * direta,

                direta
            )
        }

        // INVERSA

        val inversa =
            buscarCotacao(destino, origem)

        if (inversa != null) {

            val invertida =
                1 / inversa

            return ResultadoConversao(

                valor * invertida,

                invertida
            )
        }

        val origemUSD =
            obterCotacaoUniversal(
                origem,
                "USD"
            )

        val usdDestino =
            obterCotacaoUniversal(
                "USD",
                destino
            )

        if (
            origemUSD != null &&
            usdDestino != null
        ) {

            val valorUSD =
                valor * origemUSD

            val resultado =
                valorUSD * usdDestino

            return ResultadoConversao(

                resultado,

                usdDestino
            )
        }

        throw Exception(
            "Conversão indisponível"
        )
    }

    private suspend fun obterCotacaoUniversal(

        origem: String,
        destino: String

    ): Double? {

        val direta =
            buscarCotacao(origem, destino)

        if (direta != null) {

            return direta
        }

        val inversa =
            buscarCotacao(destino, origem)

        if (inversa != null) {

            return 1 / inversa
        }

        return null
    }

    private suspend fun buscarCotacao(

        origem: String,
        destino: String

    ): Double? {

        return try {

            val moedas =
                "$origem-$destino"

            val response =
                RetrofitClient.api
                    .getCotacao(moedas)

            if (response.isSuccessful) {

                val body = response.body()

                val chave =
                    origem + destino

                body?.get(chave)
                    ?.bid
                    ?.toDouble()

            } else {

                null
            }

        } catch (e: Exception) {

            null
        }
    }
}