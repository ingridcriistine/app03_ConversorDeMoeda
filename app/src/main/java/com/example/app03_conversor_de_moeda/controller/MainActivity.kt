package com.example.app03_conversor_de_moeda.controller

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.app03_conversor_de_moeda.databinding.ActivityMainBinding
import com.example.app03_conversor_de_moeda.model.Wallet
import java.text.NumberFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnConverter.setOnClickListener {
            startActivity(Intent(this, ConvertActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        updateBalances()
    }

    private fun updateBalances() {
        val brlFmt = NumberFormat.getNumberInstance(Locale("pt", "BR")).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        val usdFmt = NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        val btcFmt = NumberFormat.getNumberInstance(Locale.US).apply {
            minimumFractionDigits = 6
            maximumFractionDigits = 6
        }

        binding.txtReal.text = "R$ ${brlFmt.format(Wallet.brl)}"
        binding.txtDollar.text = "$ ${usdFmt.format(Wallet.usd)}"
        binding.txtBitcoin.text = "${btcFmt.format(Wallet.btc)} BTC"
    }
}
