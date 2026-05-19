// controller/MainActivity.kt

package com.example.app03_conversor_de_moeda.controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.app03_conversor_de_moeda.R
import com.example.app03_conversor_de_moeda.model.WalletManager
import com.example.app03_conversor_de_moeda.utils.CurrencyFormatter

class MainActivity : AppCompatActivity() {

    private lateinit var txtReal: TextView
    private lateinit var txtDollar: TextView
    private lateinit var txtBitcoin: TextView

    private lateinit var btnConverter: Button

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        txtReal = findViewById(R.id.txtReal)
        txtDollar = findViewById(R.id.txtDollar)
        txtBitcoin = findViewById(R.id.txtBitcoin)

        btnConverter =
            findViewById(R.id.btnConverter)

        atualizarCarteira()

        btnConverter.setOnClickListener {
            startActivity(
                Intent(this, Converter::class.java)
            )
        }
    }

    override fun onResume() {
        super.onResume()

        atualizarCarteira()
    }

    private fun atualizarCarteira() {

        txtReal.text =

            CurrencyFormatter.formatar(

                "BRL",

                WalletManager.wallet.brl
            )

        txtDollar.text =

            CurrencyFormatter.formatar(

                "USD",

                WalletManager.wallet.usd
            )

        txtBitcoin.text =

            CurrencyFormatter.formatar(

                "BTC",

                WalletManager.wallet.btc
            )
    }
}