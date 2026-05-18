package com.example.app03_conversor_de_moeda.controller

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app03_conversor_de_moeda.api.RetrofitClient
import com.example.app03_conversor_de_moeda.databinding.ActivityConvertBinding
import com.example.app03_conversor_de_moeda.model.Wallet
import kotlinx.coroutines.launch

class ConvertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConvertBinding
    private val currencies = listOf("BRL", "USD", "BTC")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConvertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencies)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerFrom.adapter = adapter
        binding.spinnerTo.adapter = adapter
        binding.spinnerFrom.setSelection(1) // USD
        binding.spinnerTo.setSelection(0)   // BRL

        binding.btnConvert.setOnClickListener { performConversion() }
    }

    private fun performConversion() {
        val from = binding.spinnerFrom.selectedItem.toString()
        val to = binding.spinnerTo.selectedItem.toString()
        val amountText = binding.etAmount.text.toString().trim()

        // 1. Moedas iguais
        if (from == to) {
            Toast.makeText(this, "Selecione moedas diferentes", Toast.LENGTH_SHORT).show()
            return
        }

        // 2. Valor inválido
        val amount = amountText.toDoubleOrNull()
        if (amount == null || amount <= 0.0) {
            binding.etAmount.error = "Informe um valor válido"
            return
        }

        // 3. Saldo insuficiente — verificado ANTES da API
        val available = when (from) {
            "BRL" -> Wallet.brl
            "USD" -> Wallet.usd
            "BTC" -> Wallet.btc
            else  -> 0.0
        }
        if (amount > available) {
            Toast.makeText(this, "Saldo insuficiente", Toast.LENGTH_SHORT).show()
            return
        }

        // 4. Ativar carregamento
        setLoadingState(true)

        // 5. Chamada assíncrona à API
        lifecycleScope.launch {
            try {
                val (pair, useMultiply) = resolveApiPair(from, to)
                val response = RetrofitClient.apiService.getRate(pair)

                val key = pair.replace("-", "")
                val rate = response[key] ?: throw Exception("Taxa não encontrada para $key")

                val bid = rate.bid.toDouble()
                val ask = rate.ask.toDouble()
                val result = if (useMultiply) amount * bid else amount / ask

                // Atualizar saldos no Wallet
                when (from) {
                    "BRL" -> Wallet.brl -= amount
                    "USD" -> Wallet.usd -= amount
                    "BTC" -> Wallet.btc -= amount
                }
                when (to) {
                    "BRL" -> Wallet.brl += result
                    "USD" -> Wallet.usd += result
                    "BTC" -> Wallet.btc += result
                }

                finish()

            } catch (e: Exception) {
                Toast.makeText(this@ConvertActivity, "Erro: ${e.message}", Toast.LENGTH_LONG).show()
            } finally {
                setLoadingState(false)
            }
        }
    }

    // Retorna o par da API e se deve multiplicar (true) ou dividir (false)
    private fun resolveApiPair(from: String, to: String): Pair<String, Boolean> =
        when ("$from->$to") {
            "USD->BRL" -> "USD-BRL" to true   // amount * bid
            "BRL->USD" -> "USD-BRL" to false  // amount / ask
            "BTC->BRL" -> "BTC-BRL" to true   // amount * bid
            "BRL->BTC" -> "BTC-BRL" to false  // amount / ask
            "BTC->USD" -> "BTC-USD" to true   // amount * bid
            "USD->BTC" -> "BTC-USD" to false  // amount / ask
            else -> throw IllegalArgumentException("Par desconhecido: $from -> $to")
        }

    private fun setLoadingState(loading: Boolean) {
        binding.progressBar.visibility = if (loading) View.VISIBLE else View.GONE
        binding.btnConvert.isEnabled = !loading
    }
}
