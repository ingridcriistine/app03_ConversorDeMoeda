package com.example.app03_conversor_de_moeda.controller

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.app03_conversor_de_moeda.R
import com.example.app03_conversor_de_moeda.model.WalletManager
import com.example.app03_conversor_de_moeda.service.CurrencyConverter
import com.example.app03_conversor_de_moeda.utils.CurrencyFormatter
import kotlinx.coroutines.launch

class Converter : AppCompatActivity() {

    private lateinit var txtCotacao: TextView

    private lateinit var imgOrigem: ImageView
    private lateinit var imgDestino: ImageView

    private lateinit var spinnerOrigem: Spinner
    private lateinit var spinnerDestino: Spinner

    private lateinit var editValor: EditText

    private lateinit var txtResultado: TextView

    private lateinit var btnConverter: Button

    private lateinit var btnVoltar: ImageButton

    private lateinit var progressBar: ProgressBar

    private val moedas = arrayOf(
        "BRL",
        "USD",
        "BTC"
    )

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)


        setContentView(R.layout.activity_conversor)

        btnVoltar =
            findViewById(R.id.btnVoltar)

        btnVoltar.setOnClickListener {
            finish()
        }

        txtCotacao =
            findViewById(R.id.txtCotacao)

        txtResultado =
            findViewById(R.id.txtResultado)

        imgOrigem =
            findViewById(R.id.imgOrigem)

        imgDestino =
            findViewById(R.id.imgDestino)

        spinnerOrigem =
            findViewById(R.id.spinnerOrigem)

        spinnerDestino =
            findViewById(R.id.spinnerDestino)

        editValor =
            findViewById(R.id.editValor)

        btnConverter =
            findViewById(R.id.btnConverter)

        progressBar =
            findViewById(R.id.progressBar)

        val adapter = ArrayAdapter(

            this,

            android.R.layout.simple_spinner_dropdown_item,

            moedas
        )

        spinnerOrigem.adapter = adapter
        spinnerDestino.adapter = adapter

        atualizarIcone("BRL", imgOrigem)
        atualizarIcone("BRL", imgDestino)

        spinnerOrigem.onItemSelectedListener =

            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(

                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long

                ) {

                    atualizarIcone(

                        spinnerOrigem.selectedItem.toString(),

                        imgOrigem
                    )
                }

                override fun onNothingSelected(

                    parent: AdapterView<*>?

                ) {

                }
            }

        spinnerDestino.onItemSelectedListener =

            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(

                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long

                ) {

                    atualizarIcone(

                        spinnerDestino.selectedItem.toString(),

                        imgDestino
                    )
                }

                override fun onNothingSelected(

                    parent: AdapterView<*>?

                ) {

                }
            }

        btnConverter.setOnClickListener {

            converter()
        }
    }

    private fun converter() {

        val origem =
            spinnerOrigem.selectedItem.toString()

        val destino =
            spinnerDestino.selectedItem.toString()

        val valorTexto =
            editValor.text.toString()

        if (valorTexto.isEmpty()) {

            Toast.makeText(

                this,

                "Digite um valor",

                Toast.LENGTH_SHORT

            ).show()

            return
        }

        val valor = valorTexto.toDouble()

        if (valor <= 0) {

            Toast.makeText(

                this,

                "Valor inválido",

                Toast.LENGTH_SHORT

            ).show()

            return
        }

        if (origem == destino) {

            Toast.makeText(

                this,

                "Selecione moedas diferentes",

                Toast.LENGTH_SHORT

            ).show()

            return
        }

        val saldoAtual = when(origem) {

            "BRL" ->
                WalletManager.wallet.brl

            "USD" ->
                WalletManager.wallet.usd

            else ->
                WalletManager.wallet.btc
        }

        if (valor > saldoAtual) {

            Toast.makeText(

                this,

                "Saldo insuficiente",

                Toast.LENGTH_SHORT

            ).show()

            return
        }

        progressBar.visibility = View.VISIBLE

        lifecycleScope.launch {

            try {

                val resultadoConversao =

                    CurrencyConverter.converter(

                        origem,
                        destino,
                        valor
                    )

                val convertido =
                    resultadoConversao.valorConvertido

                txtCotacao.text =

                    "1 $origem = %.6f $destino"
                        .format(resultadoConversao.cotacao)

                atualizarCarteira(

                    origem,
                    destino,

                    valor,
                    convertido
                )

                mostrarResultado(

                    convertido,
                    destino
                )

            } catch (e: Exception) {

                Toast.makeText(

                    this@Converter,

                    e.message,

                    Toast.LENGTH_SHORT

                ).show()

            } finally {

                progressBar.visibility =
                    View.GONE
            }
        }
    }

    private fun atualizarCarteira(

        origem: String,
        destino: String,

        valorOriginal: Double,
        valorConvertido: Double

    ) {

        when(origem) {

            "BRL" ->
                WalletManager.wallet.brl -= valorOriginal

            "USD" ->
                WalletManager.wallet.usd -= valorOriginal

            "BTC" ->
                WalletManager.wallet.btc -= valorOriginal
        }

        when(destino) {

            "BRL" ->
                WalletManager.wallet.brl += valorConvertido

            "USD" ->
                WalletManager.wallet.usd += valorConvertido

            "BTC" ->
                WalletManager.wallet.btc += valorConvertido
        }
    }

    private fun mostrarResultado(

        valor: Double,
        moeda: String

    ) {

        txtResultado.text =

            CurrencyFormatter.formatar(

                moeda,

                valor
            )
    }

    private fun atualizarIcone(

        moeda: String,

        imageView: ImageView

    ) {

        when(moeda) {

            "BRL" ->

                imageView.setImageResource(
                    R.drawable.real
                )

            "USD" ->

                imageView.setImageResource(
                    R.drawable.dolar
                )

            "BTC" ->

                imageView.setImageResource(
                    R.drawable.bitcoin
                )
        }
    }
}