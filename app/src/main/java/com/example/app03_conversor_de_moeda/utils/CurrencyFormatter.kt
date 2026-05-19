package com.example.app03_conversor_de_moeda.utils

import java.text.NumberFormat
import java.util.Locale

object CurrencyFormatter {

    fun formatar(

        moeda: String,
        valor: Double

    ): String {

        return when(moeda) {

            "BRL" -> {

                val formato =
                    NumberFormat.getCurrencyInstance(
                        Locale("pt", "BR")
                    )

                formato.format(valor)
            }

            "USD" -> {

                val formato =
                    NumberFormat.getCurrencyInstance(
                        Locale.US
                    )

                formato.format(valor)
            }

            "BTC" -> {

                String.format(
                    Locale.US,
                    "%.6f BTC",
                    valor
                )
            }

            else -> valor.toString()
        }
    }
}